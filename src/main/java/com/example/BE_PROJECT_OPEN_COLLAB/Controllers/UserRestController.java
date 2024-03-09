package com.example.BE_PROJECT_OPEN_COLLAB.Controllers;

import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BE_PROJECT_OPEN_COLLAB.Entity.User;
import com.example.BE_PROJECT_OPEN_COLLAB.Services.UserServices;

@RestController
@RequestMapping("/user")
public class UserRestController {
	
	@Autowired
	private UserServices userServices;
	
	@PostMapping("/register")
	public ResponseEntity<User> RegisterUser(@RequestBody User user) {
		
		User usersaved = userServices.saveUser(user);
		return new ResponseEntity<User>(usersaved, HttpStatus.OK);
	}
	
	@GetMapping("/login/{username}/{password}")
	public ResponseEntity<User> RegisterUser(@PathVariable("username") String username,
			@PathVariable("password") String password) {
		
		User usersaved = userServices.verifyUsernameAndPassword(username, password);
		return new ResponseEntity<User>(usersaved, HttpStatus.OK);
	}
}
