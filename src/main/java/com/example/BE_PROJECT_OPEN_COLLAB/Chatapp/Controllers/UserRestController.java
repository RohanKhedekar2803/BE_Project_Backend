package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.User;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Services.UserServices;

@RestController
@RequestMapping("/user")
public class UserRestController {
	
	@Autowired
	private UserServices userServices;
	
	@PostMapping("/register")
	public String RegisterUser(@RequestBody User user) {
		
		userServices.saveUser(user);
//		 System.out.println("hello"+ user.getUsername());
		return "hi";
	}
}
