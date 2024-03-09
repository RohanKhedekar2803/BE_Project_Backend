package com.example.BE_PROJECT_OPEN_COLLAB.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.BE_PROJECT_OPEN_COLLAB.Entity.Challenges;
import com.example.BE_PROJECT_OPEN_COLLAB.Services.ChallengesService;
import com.example.BE_PROJECT_OPEN_COLLAB.Utilities.FilterRepos;

@RestController
@RequestMapping("/challenges")
public class ChallengesController {

    @Autowired
    private ChallengesService challengesService;

    //while sending data from fronend for topics send in "topics": "['aws', 'aws-sdk', 'go']", thsi format othrwqise filter wont work
    @PostMapping("/save")
    public ResponseEntity<Challenges> addChallenge(@RequestBody Challenges challenge) {
        Challenges result = challengesService.save(challenge);
        if (result == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //get all challenges
    @PostMapping("/get/")
    public ResponseEntity<List<Challenges>> retrieveChallenges(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestBody FilterRepos filterChallenges
            ) {

        List<Challenges> result = challengesService.getChallenges(pageNo, pageSize, sortBy, filterChallenges);
//
        if (result == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    // Delete a challenge by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChallenge(@PathVariable("id") Long id) {
    	challengesService.deleteChallenge(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Update an existing challenge
    @PutMapping("/{id}")
    public ResponseEntity<Challenges> updateChallenge(@PathVariable("id") Long id, @RequestBody Challenges challenge) {
        Challenges updatedChallenge = challengesService.updateChallenge(id, challenge);
        return new ResponseEntity<>(updatedChallenge, HttpStatus.OK);
    }
    
    // get challenge by id
    @GetMapping("/{id}")
    public ResponseEntity<Challenges> getChallengeById(@PathVariable("id") Long id) {
        Challenges challenge = challengesService.getChallengeById(id);
        if (challenge != null) {
            return new ResponseEntity<>(challenge, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    //..............................................................................
    
    //get all challenges
    @PostMapping("/getbyprofile/")
    public ResponseEntity<List<Challenges>> retrieveChallengesByProfile(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "recommended") String sortBy,
            @RequestBody FilterRepos filterChallenges,
            @RequestParam(defaultValue = "") String username
            ) {
    	
    	//dummy data 
    			String[] userLanguages = {"Java", "Python", "JavaScript"};
    			String[] userTopics = {"Machine Learning", "Artificial Intelligence", "Cloud"};
    			
        List<Challenges> result = challengesService.getChallengesByProfile(pageNo, pageSize, sortBy, filterChallenges, userLanguages, userTopics, username);

        if (result == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
}
