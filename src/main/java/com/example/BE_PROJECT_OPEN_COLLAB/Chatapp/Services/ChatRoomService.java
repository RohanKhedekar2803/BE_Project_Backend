package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.ChatRoom;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories.ChatRoomRepository;

@Service
public class ChatRoomService {
	@Autowired
	private ChatRoomRepository chatroomRepository;
	
	public String getChatRoomId(String senderId , String receiverId,boolean newRoomIfNotExists) {
		
		//chatroom id
		Optional<ChatRoom> chatroom=
				chatroomRepository.findBySenderIdAndReceiverId(senderId,receiverId);
		
		if(chatroom.isPresent()) {
			return chatroom.get().getId();
		}
		
		if(newRoomIfNotExists) {
			var chatid=String.format(("%s_%s"), senderId,receiverId);
			
			//create new 2 chatrooms 
			ChatRoom senderReciverChatroom=ChatRoom.builder()
					.chatId(chatid)
					.senderId(senderId)
					.receiverId(receiverId)
					.build();
			
			ChatRoom reciverSenderChatroom=ChatRoom.builder()
					.chatId(chatid)
					.senderId(receiverId)
					.receiverId(senderId)
					.build();
			
			chatroomRepository.save(senderReciverChatroom);
			chatroomRepository.save(reciverSenderChatroom);
			
			return chatid;
		}
		
		//deadcode
		return "";
	}
	
	
}
