package com.example.BE_PROJECT_OPEN_COLLAB.Controllers;

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
import org.springframework.web.bind.annotation.PostMapping;

import com.example.BE_PROJECT_OPEN_COLLAB.Entity.ChatMessage;
import com.example.BE_PROJECT_OPEN_COLLAB.Entity.ChatNotification;
import com.example.BE_PROJECT_OPEN_COLLAB.Entity.User;
import com.example.BE_PROJECT_OPEN_COLLAB.Repositories.ChatMessageRepository;
import com.example.BE_PROJECT_OPEN_COLLAB.Services.UserServices;
import com.example.BE_PROJECT_OPEN_COLLAB.Utilities.DateTimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Builder;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
public class UserController {

	@Autowired
	private UserServices userServices;

	@Autowired
	public SimpMessagingTemplate messagingTemplatequeue;

	@Autowired
	public ChatMessageRepository chatMessageRepository;

	@MessageMapping("/user.addUser")
	@SendTo("/users/topic")
	public ChatNotification addUserInApp(@Payload ChatMessage chatmsg) {
		System.out.println("Sending to users/topic");
//		userServices.saveUser(chatmsg.getSenderId());

		ChatMessage msg = ChatMessage.builder().senderId(chatmsg.getSenderId()).receiverId("ALL")
				.timeStamp(DateTimeUtils.getCurrentDateTimeInIndia()).content(chatmsg.getContent()).chatroomId("global")
				.build();
		
		ChatMessage chat = chatMessageRepository.save(msg);
		
		ChatNotification notification = ChatNotification.builder().senderId(msg.getSenderId())
				.receiverId(msg.getReceiverId()).content(msg.getContent()).chatroomId(msg.getChatroomId())
				.messageId(msg.getChatMessageid()).build();
		return notification;
	}

	@MessageMapping("/user.disconnectUser")
	@SendTo("/users/topic")
	public User disconnect(@Payload User user) {
		userServices.dissconnectUser(user);
		return user;
	}

	@GetMapping("/getActiveUsers")
	public ResponseEntity<List<User>> getConnected() {
		return ResponseEntity.ok(userServices.getAllUsers());
	}
	

}
