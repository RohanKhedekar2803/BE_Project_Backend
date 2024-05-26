package com.example.BE_PROJECT_OPEN_COLLAB.Controllers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.BE_PROJECT_OPEN_COLLAB.Entity.ChatMessage;
import com.example.BE_PROJECT_OPEN_COLLAB.Entity.ChatNotification;
import com.example.BE_PROJECT_OPEN_COLLAB.Services.ChatMessageService;
import com.example.BE_PROJECT_OPEN_COLLAB.Utilities.DateTimeUtils;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
public class ChatController {

	@Autowired
	public ChatMessageService chatMessageService;

	@Autowired
	public SimpMessagingTemplate messagingTemplatequeue;

	@GetMapping("/chats/{senderId}/{receiverId}")
	public ResponseEntity<List<ChatMessage>> findChats(@PathVariable("senderId") String senderId,
			@PathVariable("receiverId") String receiverId) {

		return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, receiverId));
	}
	

	
	

	@MessageMapping("/chat")
	public BodyBuilder ProcessMessage(@Payload ChatMessage chatMessage) {

		boolean success= chatMessageService.sendMessage(chatMessage);
				
		if(!success)return ResponseEntity.badRequest();
				
		return ResponseEntity.ok();
	}
}
