package com.example.BE_PROJECT_OPEN_COLLAB.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.BE_PROJECT_OPEN_COLLAB.Entity.ChatMessage;
import com.example.BE_PROJECT_OPEN_COLLAB.Entity.ChatRoom;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {

	List<ChatMessage> findByChatroomId(String chatRoomId);

}
