package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotification {
	private String messageId;
	private String senderId;
	private String receiverId;
	private String content;
}
