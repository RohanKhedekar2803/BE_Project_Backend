package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ChatMessage {
	
	@Id
	private String id; 
	private String chatId;  
	private String senderId;
	private String receiverId;
	private String content;
	private Date timeStamp;
}
