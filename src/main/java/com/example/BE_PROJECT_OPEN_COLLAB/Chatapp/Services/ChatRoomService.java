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
		System.out.println("in service");
		//chatroom id
		Optional<ChatRoom> chatroom=
				chatroomRepository.findBySenderIdAndReceiverId(senderId,receiverId);
		
		if(chatroom.isPresent()) {
			return chatroom.get().getChatroomId();
		}
		
		if(newRoomIfNotExists) {
			var ChatRoomId=String.format(("%s_%s"), senderId,receiverId);
			
			//create new chatroom
			ChatRoom senderReciverChatroom=ChatRoom.builder()
					.chatroomId(ChatRoomId)
					.senderId(senderId)
					.receiverId(receiverId)
					.build();
			
			
			chatroomRepository.save(senderReciverChatroom);

			System.out.println("saved" + senderReciverChatroom);
			System.out.println("saved" + ChatRoomId);
			return ChatRoomId;
		}
		
		//deadcode
		return "";
	}
	
	
}
