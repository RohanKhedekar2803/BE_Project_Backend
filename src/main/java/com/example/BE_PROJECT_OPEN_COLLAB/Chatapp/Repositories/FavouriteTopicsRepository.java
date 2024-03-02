package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.FavouriteLanguage;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.FavouriteTopic;

@Repository
public interface FavouriteTopicsRepository extends CrudRepository<FavouriteTopic, Long>{

}
