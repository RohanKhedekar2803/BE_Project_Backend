package com.example.BE_PROJECT_OPEN_COLLAB.GroupChat;

import java.util.ArrayList;

import com.example.BE_PROJECT_OPEN_COLLAB.Entity.ChatRoom;
import com.example.BE_PROJECT_OPEN_COLLAB.Entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
public class GroupChatEntity {
	@Id
	public String chatroomId;
	private String owner;
	private String Name;
	private String Description;
	private String CreatedAt;
	
	 @ManyToMany
	private ArrayList<User> members;
}
