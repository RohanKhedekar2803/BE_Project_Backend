package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity;

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
public class ChatRoom {

	@Id
	public String chatroomId;
	private String senderId;
	private String receiverId;
}
