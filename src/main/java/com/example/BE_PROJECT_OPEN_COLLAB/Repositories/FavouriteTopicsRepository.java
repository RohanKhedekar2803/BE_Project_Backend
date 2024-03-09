package com.example.BE_PROJECT_OPEN_COLLAB.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.BE_PROJECT_OPEN_COLLAB.Entity.FavouriteLanguage;
import com.example.BE_PROJECT_OPEN_COLLAB.Entity.FavouriteTopic;

@Repository
public interface FavouriteTopicsRepository extends CrudRepository<FavouriteTopic, Long>{

}
