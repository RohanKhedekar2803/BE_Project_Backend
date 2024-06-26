package com.example.BE_PROJECT_OPEN_COLLAB.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BE_PROJECT_OPEN_COLLAB.Entity.FavouriteLanguage;
import com.example.BE_PROJECT_OPEN_COLLAB.Entity.User;
import com.example.BE_PROJECT_OPEN_COLLAB.Utilities.Status;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	List<User> findAllByStatus(Status online);

	Optional<User> findByUsernameAndPassword(String username, String password);

	User findByUsername(String username);
	
	// Retrieve all favorite languages for a user with a specific username
//	List<FavouriteLanguage> findAllFavouriteLanguagesByUsername(String username);

}
