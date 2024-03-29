package com.example.BE_PROJECT_OPEN_COLLAB.Entity;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long chatMessageid;

	private String chatroomId;
	private String senderId;
	private String receiverId;
	private String content;
	private String timeStamp;
}
