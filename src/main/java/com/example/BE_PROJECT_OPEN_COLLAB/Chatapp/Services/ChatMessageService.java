package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Services;

import java.io.Serializable;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.ChatMessage;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.ChatNotification;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories.ChatMessageRepository;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories.ChatRoomRepository;
import com.example.BE_PROJECT_OPEN_COLLAB.Utilities.DateTimeUtils;

@Service
public class ChatMessageService {
	@Autowired
	public ChatRoomRepository chatRoomRepository;

	@Autowired
	public ChatRoomService chatRoomService;

	@Autowired
	public SimpMessagingTemplate messagingTemplatequeue;

	@Autowired
	public ChatMessageRepository chatMessageRepository;

	public ChatMessage saveMessageInDatabase(ChatMessage chatMessage) {
		var chatId = chatRoomService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getReceiverId(), true);
		chatMessage.setChatroomId(chatId);
		return chatRoomRepository.save(chatMessage);
	}

	public List<ChatMessage> findChatMessages(String senderId, String ReceiverId) {

		String ChatRoomId = chatRoomService.getChatRoomId(senderId, ReceiverId, false);

		List<ChatMessage> list = chatMessageRepository.findByChatroomId(ChatRoomId);

		return list;
	}
	
	public boolean sendMessage(ChatMessage chatMessage) {
			boolean status=false;
			try {
				chatMessage.setTimeStamp(DateTimeUtils.getCurrentDateTimeInIndia());
				ChatMessage savedMessage = saveMessageInDatabase(chatMessage);
				
				messagingTemplatequeue.convertAndSend("/users/" + chatMessage.getReceiverId() + "/queue/messages",
						(Serializable) ChatNotification.builder().messageId(chatMessage.getChatMessageid())
								.chatroomId(chatMessage.getChatroomId()).senderId(chatMessage.getSenderId())
								.receiverId(chatMessage.getReceiverId()).content(chatMessage.getContent()).build());
			
				status=true;
			}catch (Exception e) {
				System.out.println("exception in msg sednign but itgs handled");
			}
			return status;
			
		
	}

}
