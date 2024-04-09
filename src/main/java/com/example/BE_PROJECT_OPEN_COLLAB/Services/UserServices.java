package com.example.BE_PROJECT_OPEN_COLLAB.Services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import com.example.BE_PROJECT_OPEN_COLLAB.CustomException;
import com.example.BE_PROJECT_OPEN_COLLAB.CosineSimilarityAlgorithm.CosineSimilarity;
import com.example.BE_PROJECT_OPEN_COLLAB.Entity.Challenges;
import com.example.BE_PROJECT_OPEN_COLLAB.Entity.FavouriteLanguage;
import com.example.BE_PROJECT_OPEN_COLLAB.Entity.FavouriteTopic;
import com.example.BE_PROJECT_OPEN_COLLAB.Entity.User;
import com.example.BE_PROJECT_OPEN_COLLAB.Models.UserResponse;
import com.example.BE_PROJECT_OPEN_COLLAB.Repositories.FavouriteLanguagesRepository;
import com.example.BE_PROJECT_OPEN_COLLAB.Repositories.FavouriteTopicsRepository;
import com.example.BE_PROJECT_OPEN_COLLAB.Repositories.UserRepository;
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
	
	@Autowired
	private CosineSimilarity cosineSimilarity;

	public UserResponse saveUser(User newUser) throws CustomException{
		
		
			Optional<User> checkUser=userRepository.findById(newUser.getUsername());
			if(checkUser.isPresent()) {
				System.out.println("already there");
				throw new CustomException("user already exists");
			}			
			//create user
			User user = User.builder()
					.status(Status.Online)
					.password(newUser.getPassword())
					.nickname(newUser.getNickname())
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
			
			return UserResponse.builder().username(user.getUsername()).nickname(user.getNickname()).
					isOrganization(user.getIsOrganization()).favouriteLanguages(user.getFavouriteLanguages()).
					favouriteTopic(user.getFavouriteTopic()).build();
		
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
	
	public UserResponse verifyUsernameAndPassword(String Username, String Password){

		Optional<User> checkUser=userRepository.findById(Username);
		if(checkUser.isEmpty()) {

			throw new CustomException("username is not valid");
		}
			if(!checkUser.get().getPassword().equals(Password)) {
				throw new CustomException("username & password do not match");
			}
			User user= userRepository.findByUsername(Username);
			
			return UserResponse.builder().username(user.getUsername()).nickname(user.getNickname()).
					isOrganization(user.getIsOrganization()).favouriteLanguages(user.getFavouriteLanguages()).
					favouriteTopic(user.getFavouriteTopic()).build();
		
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
	            favoriteTopics.add("'"+topic.getTopicname()+"'");
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

	
	public UserResponse getUserByUsername(String username) {
	    User user=userRepository.findByUsername(username);
//		if(user == null) {
//
//			throw new CustomException("username is not valid");
//		}
		return UserResponse.builder().username(user.getUsername()).nickname(user.getNickname()).
				isOrganization(user.getIsOrganization()).favouriteLanguages(user.getFavouriteLanguages()).
				favouriteTopic(user.getFavouriteTopic()).build();
	
	/**/
	
	}
	public List<FavouriteLanguage> getlanguage() {
		return (List<FavouriteLanguage>) favouriteLanguagesRepository.findAll();
	}

	public List<FavouriteTopic> getTopics() {
		
		return (List<FavouriteTopic>) favouriteTopicsRepository.findAll();
	}

	public List<UserResponse> getRecomendedFriends(String Username){
		Optional<User> user=userRepository.findById(Username);
		if(user.isEmpty()) {
			throw new CustomException(Username + "not found ");
		}
		List<User> allUsers=userRepository.findAll();
		allUsers.removeIf( (User)->  User.getUsername()==Username );
		
		
		//ml model
		String[] userLanguages = {};
		String[] userTopics = {};
	     userLanguages =getFavoriteLanguagesByUsernameFromDb(Username);
	     userTopics =getFavoriteTopicsByUsernameFromDb(Username);
        List<List<String>> OtherUsersdocuments = extractLanguagesAndTopics(allUsers);
//
//        // Sort challenges using TF-IDF algorithm based on user languages and topics
        List<Integer> sortedIndices = cosineSimilarity.sortPeopleByCosineSimilarity(userLanguages, userTopics, OtherUsersdocuments);
//
//        // Map the sorted indices to the list of challenges
        List<User> sortedusers= new ArrayList<>();
        for (int index : sortedIndices) {
        	sortedusers.add(allUsers.get(index));
        }
		
		
		
		
		
		
		// ml end
		
		
		List <UserResponse>list= new ArrayList<>();
		for(User User: sortedusers) {
			list.add(UserResponse.builder()
					.nickname(User.getNickname())
					.username(User.getUsername())
					.isOrganization(User.getIsOrganization())
					.build());
		}
		
		return list;
	}

	private List<List<String>> extractLanguagesAndTopics(List<User> allUsers) {
		 List<List<String>> documents = new ArrayList<>();
	        for (User user : allUsers) {
	            List<String> usersdata = new ArrayList<>();
	            // Add languages and topics from the challenge to the document
	            for(FavouriteLanguage lan : user.getFavouriteLanguages()) {
	            	 usersdata.add(lan.getLanguageName());
	            }
	            
	            for(FavouriteTopic topic : user.getFavouriteTopic()) {
	            	 usersdata.add(topic.getTopicname());
	            }
	           
	            documents.add(usersdata);
	        }
	        return documents;
	}
	
}
