package com.example.BE_PROJECT_OPEN_COLLAB.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BE_PROJECT_OPEN_COLLAB.Entity.ChatMessage;
import com.example.BE_PROJECT_OPEN_COLLAB.Entity.FavouriteLanguage;
import com.example.BE_PROJECT_OPEN_COLLAB.Entity.User;
import com.example.BE_PROJECT_OPEN_COLLAB.Models.UserResponse;
import com.example.BE_PROJECT_OPEN_COLLAB.Services.ChatMessageService;
import com.example.BE_PROJECT_OPEN_COLLAB.Services.UserServices;

@RestController
@RequestMapping("/user")
public class UserRestController {
	
	@Autowired
	private UserServices userServices;
	
	@Autowired
	private ChatMessageService chatMessageService;
	
	@PostMapping("/register")
	public ResponseEntity<UserResponse> RegisterUser(@RequestBody User user) {
		
		UserResponse usersaved = userServices.saveUser(user);
		return new ResponseEntity<UserResponse>( usersaved, HttpStatus.OK);
	}
	
	@GetMapping("/login/{username}/{password}")
	public ResponseEntity<UserResponse> login(@PathVariable("username") String username,
			@PathVariable("password") String password) {
		UserResponse usersaved = userServices.verifyUsernameAndPassword(username, password);
		return new ResponseEntity<UserResponse>(usersaved, HttpStatus.OK);
	}
	
	@GetMapping("/getUser/{username}")
	public ResponseEntity<String> getUser(@PathVariable("username") String username) {
		UserResponse usersaved = userServices.getUserByUsername(username);
		return new ResponseEntity<String>("usersaved", HttpStatus.OK);
	}
	
	@GetMapping("/getRecommendedFriends/{username}")
	public ResponseEntity<List<UserResponse>> getFriends(@PathVariable("username") String username){
	    List<UserResponse> users=userServices.getRecomendedFriends(username);
	    
	    return new ResponseEntity<List<UserResponse>>(users,HttpStatus.OK);
	}
	
	
	@PostMapping("/savechat/{senderId}/{receiverId}")
	public ResponseEntity<ChatMessage> UpdateChats(@PathVariable("senderId") String senderId,
			@PathVariable("receiverId") String receiverId,
			@RequestBody ChatMessage chatMessage) {

		return ResponseEntity.ok(chatMessageService.saveMessageInDatabase(chatMessage));
	}
	
	@GetMapping("/chats/{senderId}/{receiverId}")
	public ResponseEntity<List<ChatMessage>> findChats(@PathVariable("senderId") String senderId,
			@PathVariable("receiverId") String receiverId) {

		return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, receiverId));
	}

	

}
