package com.example.BE_PROJECT_OPEN_COLLAB.Repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.BE_PROJECT_OPEN_COLLAB.Entity.FavouriteLanguage;
import com.example.BE_PROJECT_OPEN_COLLAB.Entity.User;

@Repository
public interface FavouriteLanguagesRepository extends CrudRepository<FavouriteLanguage, Long>{
	
	List<FavouriteLanguage> findAllByUser(User user);
	
	List<FavouriteLanguage> findAllByUser_Username(String username);
}
