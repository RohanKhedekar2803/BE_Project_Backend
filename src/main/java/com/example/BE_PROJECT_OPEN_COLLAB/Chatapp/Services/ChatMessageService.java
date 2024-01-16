package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.ChatMessage;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories.ChatRoomRepository;

@Service
public class ChatMessageService {
	@Autowired
	public ChatRoomRepository chatRoomRepository;
	
	@Autowired
	public ChatRoomService chatRoomService;
	
	public ChatMessage save(ChatMessage chatMessage) {
		var chatId= chatRoomService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getReceiverId(), true);
		chatMessage.setChatId(chatId);
		return chatRoomRepository.save(chatMessage);
	}
	
	public List<ChatMessage> findChatMessages(String senderId,String ReceiverId){
		
		var chatId=chatRoomService.getChatRoomId(senderId, ReceiverId, false);
		 List<ChatMessage> list=chatRoomRepository.findAllByChatId(chatId);
		 
		return list;
	}
	
}
