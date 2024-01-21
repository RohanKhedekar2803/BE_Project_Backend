package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.ChatMessage;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.ChatRoom;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories.ChatMessageRepository;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories.ChatRoomRepository;

@Service
public class ChatMessageService {
	@Autowired
	public ChatRoomRepository chatRoomRepository;
	
	@Autowired
	public ChatRoomService chatRoomService;
	
	@Autowired
	public ChatMessageRepository chatMessageRepository;
	
	public ChatMessage save(ChatMessage chatMessage) {
		var chatId= chatRoomService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getReceiverId(), true);
		chatMessage.setChatroomId(chatId);
		return chatRoomRepository.save(chatMessage);
	}
	
	public List<ChatMessage> findChatMessages(String senderId,String ReceiverId){
		
		String ChatRoomId=chatRoomService.getChatRoomId(senderId, ReceiverId, false);

		List<ChatMessage> list = chatMessageRepository.findByChatroomId(ChatRoomId);
		
		return list;
	}
	
}
