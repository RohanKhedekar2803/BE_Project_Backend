package com.example.BE_PROJECT_OPEN_COLLAB.Entity;

import java.util.List;

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
public class Challenges {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		public String createdBy;
		public String nameOfOrganization;
		public String nameChallenge;
		public String problemStatement;
		public String description;
		public String theme;
		public String githubUrl;
		public String createdAt;
		public String language;
		public String topics;
		public String startDateAndTime;
		public String endDateAndTime;
		public boolean isHackathon;
		public boolean isBounty;
		public boolean isHiring;
		public boolean isSolo;
		public Long maxPeopleinTeam;
		public Long minPeopleinTeam;
		public Long salaryPerYear;
		public Long prize;
		public List<String> relatedLinks;
}
