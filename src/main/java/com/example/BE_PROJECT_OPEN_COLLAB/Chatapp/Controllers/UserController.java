package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.User;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Services.UserServices;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
public class UserController {

	@Autowired
	private UserServices userServices;
	
	@Autowired
	public SimpMessagingTemplate messagingTemplatequeue;
	
	@MessageMapping("/user.addUser")
	@SendTo("/users/topic")
	public User addUser(@Payload User user) {
		System.out.println("Sending to usersa/topic");
		userServices.saveUser(user);
		return user;
	}
	
	@MessageMapping("/user.disconnectUser")
	@SendTo("/users/topic")
	public User disconnect(@Payload User user) {
		userServices.dissconnectUser(user);
		return user;
	}
	
	@GetMapping("/getActiveUsers")
	public ResponseEntity<List<User>> getConnected(){
		return ResponseEntity.ok(userServices.getAllUsers());
	}
	
	
}
