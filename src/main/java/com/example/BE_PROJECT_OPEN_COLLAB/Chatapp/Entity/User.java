package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity;

import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Status;

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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
	@Id
	String username;
	String nickname;
	private Status status;
	
}
