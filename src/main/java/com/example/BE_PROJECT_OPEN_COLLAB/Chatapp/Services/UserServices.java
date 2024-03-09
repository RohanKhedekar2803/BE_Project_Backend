package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import com.example.BE_PROJECT_OPEN_COLLAB.CustomException;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.FavouriteLanguage;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.FavouriteTopic;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.User;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories.FavouriteLanguagesRepository;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories.FavouriteTopicsRepository;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories.UserRepository;
import com.example.BE_PROJECT_OPEN_COLLAB.Utilities.Status;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserServices {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FavouriteLanguagesRepository favouriteLanguagesRepository;
	
	@Autowired
	private FavouriteTopicsRepository favouriteTopicsRepository;
	
	@Autowired
	private RestTemplate restTemplate;

	public User saveUser(User newUser) throws CustomException{
		
		
			Optional<User> checkUser=userRepository.findById(newUser.getUsername());
			if(checkUser.isPresent()) {
				System.out.println("already there");
				throw new CustomException("user already exists");
			}			
			//create user
			User user = User.builder()
					.status(Status.Online)
					.password(newUser.getPassword())
					.username(newUser.getUsername())
					.isOrganization(newUser.getIsOrganization())
					.build();
			
			
			//set its languages and topics
			
			List<String> langs=getLanguagesUsedByUser(user.getUsername());
			List<FavouriteLanguage> languageList=saveAllFavouriteLanguages(langs);
			user.setFavouriteLanguages(languageList);
				
			List<String> topics=getTopicsUsedByUser(user.getUsername());
			List<FavouriteTopic> topicList=saveAllFavouriteTopics(topics);
			user.setFavouriteTopic(topicList);
			
			User saveduser = userRepository.save(user);
			return saveduser;
		
	}

	public void dissconnectUser(User user) {
		var storedUser = userRepository.findById(user.getUsername()).orElse(null);
		if (storedUser != null) {
			storedUser.setStatus(Status.Offline);
			userRepository.save(storedUser);
		}
	}

	public List<User> getAllUsers() {
		return userRepository.findAllByStatus(Status.Online);
	}
	
	public User verifyUsernameAndPassword(String Username, String Password){
		
		Optional<User> checkUser=userRepository.findById(Username);
		if(checkUser.isEmpty()) {
			System.out.println("already there");
			throw new CustomException("username is not valid");
		}else {
			if(!checkUser.get().getPassword().equals(Password)) {
				throw new CustomException("username & password do not match");
			}
		}
		return checkUser.get();
	}
	//helper for services 
	public List<String> getLanguagesUsedByUser(String username) {
		System.out.println("in it");
	    String reposUrl = "https://api.github.com/users/" + username + "/repos";
	    ObjectMapper mapper = new ObjectMapper();

	    try {
	        JsonNode reposJson = restTemplate.getForObject(reposUrl, JsonNode.class);
	        System.out.println(reposJson+ "random");
	       
	        // Iterate through repositories and extract languages
	        List<String> languages = new ArrayList<>();
	        for (JsonNode repo : reposJson) {
	            String language = repo.get("language").asText();
	            if(!language.equals("null")  && !languages.contains(language)) {
	            	languages.add(language);
	            }
	        }
	        
	        return languages;
	    } catch (HttpClientErrorException ex) {
	        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
	        	System.out.println("in error");
	            return Collections.emptyList();
	        } else {
	            // Handle other potential errors (e.g., log the exception)
	            throw ex;
	        }
	    }
	
	}
	
	public List<String> getTopicsUsedByUser(String username) {
		System.out.println("in it");
	    String reposUrl = "https://api.github.com/users/" + username + "/repos";
	    ObjectMapper mapper = new ObjectMapper();

	    try {
	        JsonNode reposJson = restTemplate.getForObject(reposUrl, JsonNode.class);
	        System.out.println(reposJson+ "random");
	       
	        // Iterate through repositories and extract topics generalyy absent
	        List<String> topics = new ArrayList<>();
	        for (JsonNode repo : reposJson) {
	            String topic = repo.get("topics").asText();
	            
	            if(!topic.equals("") && !topic.equals(null) && !topic.equals("null")  && !topics.contains(topic)) {
	            	topics.add(topic);
	            	System.out.println(topic +  "its topic");
	            }
	        }
	        
	        return topics;
	    } catch (HttpClientErrorException ex) {
	        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
	        	System.out.println("in error");
	            return Collections.emptyList();
	        } else {
	            // Handle other potential errors (e.g., log the exception)
	            throw ex;
	        }
	    }
	
	}
	
	public List<FavouriteLanguage> saveAllFavouriteLanguages(List<String> languages) {
        List<FavouriteLanguage> favouriteLanguages = new ArrayList<>();
        for (String language : languages) {
            FavouriteLanguage favouriteLanguage = new FavouriteLanguage();
            favouriteLanguage.setLanguageName("'"+language + "'");
            favouriteLanguages.add(favouriteLanguage);
        }
        return (List<FavouriteLanguage>) favouriteLanguagesRepository.saveAll(favouriteLanguages);
    }
	
	public List<FavouriteTopic> saveAllFavouriteTopics(List<String> Topics) {
        List<FavouriteTopic> favouriteTopics = new ArrayList<>();
        for (String topic : Topics) {
            FavouriteTopic favouriteTopic = new FavouriteTopic();
            favouriteTopic.setTopicname(topic);
            favouriteTopics.add(favouriteTopic);
        }
        return (List<FavouriteTopic>) favouriteTopicsRepository.saveAll(favouriteTopics);
    }
	
	// fopr other files
	public String[] getFavoriteTopicsByUsernameFromDb(String username) {
	    Optional<User> user = userRepository.findById(username);
	    if (user.isPresent() && user.get().getFavouriteTopic() != null) {
	        List<String> favoriteTopics = new ArrayList<>();
	        for (FavouriteTopic topic : user.get().getFavouriteTopic()) {
	            favoriteTopics.add(topic.getTopicname());
	        }
	        return favoriteTopics.toArray(new String[favoriteTopics.size()]);
	    }
	    return new String[0];
	}

	public String[] getFavoriteLanguagesByUsernameFromDb(String username) {
	    Optional<User> user = userRepository.findById(username);
	    if (user.isPresent() && user.get().getFavouriteLanguages() != null) {
	        List<String> favoriteLanguages = new ArrayList<>();
	        for (FavouriteLanguage language : user.get().getFavouriteLanguages()) {
	            favoriteLanguages.add(language.getLanguageName());
	        }
	        return favoriteLanguages.toArray(new String[favoriteLanguages.size()]);
	    }
	    if(user.isEmpty()) {
	    	throw new CustomException(username + "doent exists in db ");
	    }
	    return new String[0];
	}

}
