package com.example.BE_PROJECT_OPEN_COLLAB.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.BE_PROJECT_OPEN_COLLAB.Entity.ChatMessage;
import com.example.BE_PROJECT_OPEN_COLLAB.Entity.ChatRoom;

@Repository
public interface ChatRoomRepository extends CrudRepository<ChatRoom, String> {

	Optional<ChatRoom> findBySenderIdAndReceiverId(String senderId, String receiverId);

	ChatMessage save(ChatMessage chatMessage);

}
