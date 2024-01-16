package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.ChatMessage;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.ChatNotification;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Services.ChatMessageService;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
public class ChatController {
	
	@Autowired
	public ChatMessageService chatMessageService;
	
	@Autowired
	public SimpMessagingTemplate messagingTemplatequeue;
	
	@GetMapping("/chats/{senderId}/{receiverId}")
	public ResponseEntity<List<ChatMessage>> findChats(
			@PathVariable("senderId") String senderId,
			@PathVariable("receiverId") String receiverId
			){
		return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, receiverId));
	}
	
	@MessageMapping("/chat")
	public void ProcessMessage(@Payload ChatMessage chatMessage) {
		ChatMessage	savedMessage=chatMessageService.save(chatMessage);
		messagingTemplatequeue.convertAndSendToUser(chatMessage.getReceiverId(),"/queue/messages",
				ChatNotification.builder()
				.messageId(chatMessage.getChatId())
				.senderId(chatMessage.getSenderId())
				.receiverId(chatMessage.getReceiverId())
				.content(chatMessage.getContent())
				.build());
	}
}