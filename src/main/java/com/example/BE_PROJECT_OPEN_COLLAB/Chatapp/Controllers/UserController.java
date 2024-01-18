package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Controllers;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.ChatMessage;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.User;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories.ChatMessageRepository;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Services.UserServices;

import java.text.SimpleDateFormat;  
import java.util.Date;

import lombok.Builder;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
public class UserController {
	
	SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
    Date date = new Date(); 
	@Autowired
	private UserServices userServices;
	
	@Autowired
	public SimpMessagingTemplate messagingTemplatequeue;
	
	@Autowired
	public ChatMessageRepository chatMessageRepository;
	
	@MessageMapping("/user.addUser")
	@SendTo("/users/topic")
	public ChatMessage addUser(@Payload ChatMessage chatmsg) {
		System.out.println("Sending to users/topic");
		userServices.saveUser(chatmsg.getSenderId());
		
		ChatMessage msg= ChatMessage.builder()
				.senderId(chatmsg.getSenderId())
				.receiverId("ALL")
				.timeStamp(date)
				.content(chatmsg.getContent())
				.build();
		return chatMessageRepository.save(msg);
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
