package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Status;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,String>{

	List<User> findAllByStatus(Status online);

}