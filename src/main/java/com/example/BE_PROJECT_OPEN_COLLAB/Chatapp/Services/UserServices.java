package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Status;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.User;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories.UserRepository;

@Service
public class UserServices {
	
	@Autowired
	private UserRepository userRepository;
	
	public void saveUser(User user){
		user.setStatus(Status.Online);
		userRepository.save(user);
	}
	
	public void dissconnectUser(User user){
		var storedUser=userRepository.findById(user.getNickname()).orElse(null);
		if(storedUser!=null) {
			storedUser.setStatus(Status.Offline);
			userRepository.save(storedUser);
		}
	}
	
	public List<User> getAllUsers(){
		return userRepository.findAllByStatus(Status.Online);
	}
}
