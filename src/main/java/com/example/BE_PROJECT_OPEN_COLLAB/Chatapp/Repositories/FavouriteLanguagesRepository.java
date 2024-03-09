package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.FavouriteLanguage;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.User;

@Repository
public interface FavouriteLanguagesRepository extends CrudRepository<FavouriteLanguage, Long>{
	
	List<FavouriteLanguage> findAllByUser(User user);
	
	List<FavouriteLanguage> findAllByUser_Username(String username);
}
