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
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Status;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.FavouriteLanguage;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.User;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories.FavouriteLanguagesRepository;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories.FavouriteTopicsRepository;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories.UserRepository;
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

	public void saveUser(User newUser) throws CustomException{
		
		    
		System.out.println(newUser.getUsername());
		
			Optional<User> checkUser=userRepository.findById(newUser.getUsername());
			if(checkUser.isPresent()) {
				System.out.println("already there");
				throw new CustomException("user already exists");
			}
//			
			//create user
			User user = User.builder()
					.status(Status.Online)
					.password(newUser.getPassword())
					.username(newUser.getUsername())
					.build();
			
			
			//set its languages and topics
			
			List<String> langs=getLanguagesUsedByUser(user.getUsername());
			List<FavouriteLanguage> languageList=saveAllFavouriteLanguages(langs);
			user.setFavouriteLanguages(languageList);
				
//			List<String> topics=getLanguagesUsedByUser(user.getUsername());
//			ArrayList<FavouriteLanguage> topicList=favouriteLanguagesRepository.saveAll(topics);
//			user.setFavouriteLanguages(topicList);
			
			userRepository.save(user);
		
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
	
	
	@Autowired
	private RestTemplate restTemplate;
	
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
	            if(!language.equals("null")) {
	            	languages.add(language);
	            }
	        }
	        
	        return languages;
	    } catch (HttpClientErrorException ex) {
	        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
	        	System.out.println("in error");
	            // User not found or no public repositories
	            return Collections.emptyList();
	        } else {
	            // Handle other potential errors (e.g., log the exception)
	            throw ex;
	        }
	    }
	
	}
	
	public List<String> getTopicsUsedByUser(String username) {
	    String reposUrl = "https://api.github.com/users/" + username + "/repos";
	    JSONArray reposArray = restTemplate.getForObject(reposUrl, JSONArray.class);

	    Map<String, Integer> topicsMap = new HashMap<>();
	    for (int i = 0; i < reposArray.length(); i++) {
	        JSONObject repoObject = reposArray.getJSONObject(i);
	        String topicsUrl = repoObject.getString("topics_url");
	        JSONArray topicsArray = restTemplate.getForObject(topicsUrl, JSONArray.class);

	        for (int j = 0; j < topicsArray.length(); j++) {
	            String topic = topicsArray.getString(j);

	            // Count occurrences of each topic
	            topicsMap.merge(topic, 1, Integer::sum);
	        }
	    }

	    // Sort topics based on occurrence count
	    List<String> sortedTopics = topicsMap.entrySet().stream()
	                            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
	                            .map(Map.Entry::getKey)
	                            .collect(Collectors.toList());

	    return sortedTopics;
	}
	
	public List<FavouriteLanguage> saveAllFavouriteLanguages(List<String> languages) {
        List<FavouriteLanguage> favouriteLanguages = new ArrayList<>();
        for (String language : languages) {
            FavouriteLanguage favouriteLanguage = new FavouriteLanguage();
            favouriteLanguage.setLanguageName(language);
            favouriteLanguages.add(favouriteLanguage);
        }
        return (List<FavouriteLanguage>) favouriteLanguagesRepository.saveAll(favouriteLanguages);
    }

}
