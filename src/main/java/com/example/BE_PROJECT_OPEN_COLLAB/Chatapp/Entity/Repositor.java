package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

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
public class Repositor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
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
