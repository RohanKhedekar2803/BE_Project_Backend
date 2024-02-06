package com.example.BE_PROJECT_OPEN_COLLAB.Models;

import java.time.LocalDateTime;

import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.Repositor;

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
public class RepositoryRequest {
	
	public String name;
	public String description;
	public String url;
	public String createdAt;
	public String homepage;
	public Integer size;
	public Integer stars;
	public Integer forks;
	public String language;
	public String topics;
}
