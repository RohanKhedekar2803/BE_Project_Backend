package com.example.BE_PROJECT_OPEN_COLLAB.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BE_PROJECT_OPEN_COLLAB.Entity.FavouriteLanguage;
import com.example.BE_PROJECT_OPEN_COLLAB.Entity.FavouriteTopic;
import com.example.BE_PROJECT_OPEN_COLLAB.Services.UserServices;

@RequestMapping("/utils")
@RestController
public class UtilitiesController {
	
	@Autowired
	private UserServices userServices;
	
	@GetMapping("/getlang")
	public ResponseEntity<List<FavouriteLanguage>> getLanguages() {

		return  new ResponseEntity<List<FavouriteLanguage>>(userServices.getlanguage(), HttpStatus.OK);

}
	
	@GetMapping("/gettopics")
	public ResponseEntity<List<FavouriteTopic>> getTopics() {

			return  new ResponseEntity<List<FavouriteTopic>>(userServices.getTopics(), HttpStatus.OK);	
	}
}
