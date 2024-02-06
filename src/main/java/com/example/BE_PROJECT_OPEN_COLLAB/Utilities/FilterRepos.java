package com.example.BE_PROJECT_OPEN_COLLAB.Utilities;

import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Status;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.User;

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
public class FilterRepos {
	String hasLanguage;
	String hasTopic;
//	Long moreThanStars=(long) 0;
//	Long moreThanForks=(long) 0;
//	Long lessThanStars=(long) 0;
//	Long lessThanForks=(long) 0;
}
