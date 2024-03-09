package com.example.BE_PROJECT_OPEN_COLLAB.Entity;

import java.io.Serializable;

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
public class ChatNotification implements Serializable {
	private Long messageId;
	private String chatroomId;
	private String senderId;
	private String receiverId;
	private String content;
}
