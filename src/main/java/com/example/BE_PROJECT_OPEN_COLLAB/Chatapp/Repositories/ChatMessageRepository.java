package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.ChatMessage;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long>{

}
