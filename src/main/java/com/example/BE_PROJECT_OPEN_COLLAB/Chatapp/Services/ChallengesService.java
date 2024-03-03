package com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.BE_PROJECT_OPEN_COLLAB.CustomException;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Entity.Challenges;
import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories.ChallengesRepository;
import com.example.BE_PROJECT_OPEN_COLLAB.Utilities.FilterRepos;

@Service
public class ChallengesService {

    @Autowired
    private ChallengesRepository challengesRepository;

    public Challenges save(Challenges challenge) {
        return challengesRepository.save(challenge);
    }
    
    //while sending data from fronend for topics send in "topics": "['aws', 'aws-sdk', 'go']", thsi format othrwqise filter wont work
    public List<Challenges> getChallenges(Integer pageNo, Integer pageSize, String sortBy,
            FilterRepos filterChallenges) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, sortBy));
        Specification<Challenges> spec = Specification.where(null);

        if (filterChallenges != null) {
            String language = filterChallenges.getHasLanguage();
            String topic = filterChallenges.getHasTopic();
            
            // Remove leading and trailing whitespaces
            if (language != null) {
                language = language.trim();
                if (!language.isEmpty()) {
                    spec = spec.and(withHasLanguage(language));
                }
            }
            
            // Remove leading and trailing whitespaces
            if (topic != null) {
                topic = topic.trim();
                if (!topic.isEmpty()) {
                    spec = spec.and(withTopicContaining(topic));
                }
            }
        }

        Page<Challenges> pagedResult = challengesRepository.findAll(spec, paging);

        return pagedResult.getContent();
    }

 // Delete a challenge by ID
    public void deleteChallenge(Long id) {
    	Optional<Challenges> optionalChallenge = challengesRepository.findById(id);
    	if (optionalChallenge.isPresent()) {
    		challengesRepository.deleteById(id);
    	}else {
    		throw new CustomException(id + " is not valid challenge id");
    	}
        
    }

    // Update an existing challenge
    public Challenges updateChallenge(Long id, Challenges newChallenge) {
        Optional<Challenges> optionalChallenge = challengesRepository.findById(id);
        if (optionalChallenge.isPresent()) {
            Challenges existingChallenge = optionalChallenge.get();
            // Update properties of existingChallenge with newChallenge
            existingChallenge.setCreatedBy(newChallenge.getCreatedBy());
            existingChallenge.setNameOfOrganization(newChallenge.getNameOfOrganization());
            existingChallenge.setNameChallenge(newChallenge.getNameChallenge());
            existingChallenge.setProblemStatement(newChallenge.getProblemStatement());
            existingChallenge.setDescription(newChallenge.getDescription());
            existingChallenge.setTheme(newChallenge.getTheme());
            existingChallenge.setGithubUrl(newChallenge.getGithubUrl());
            existingChallenge.setCreatedAt(newChallenge.getCreatedAt());
            existingChallenge.setLanguage(newChallenge.getLanguage());
            existingChallenge.setTopics(newChallenge.getTopics());
            existingChallenge.setStartDateAndTime(newChallenge.getStartDateAndTime());
            existingChallenge.setEndDateAndTime(newChallenge.getEndDateAndTime());
            existingChallenge.setHackathon(newChallenge.isHackathon());
            existingChallenge.setBounty(newChallenge.isBounty());
            existingChallenge.setHiring(newChallenge.isHiring());
            existingChallenge.setSolo(newChallenge.isSolo());
            existingChallenge.setMaxPeopleinTeam(newChallenge.getMaxPeopleinTeam());
            existingChallenge.setMinPeopleinTeam(newChallenge.getMinPeopleinTeam());
            existingChallenge.setSalaryPerYear(newChallenge.getSalaryPerYear());
            existingChallenge.setPrize(newChallenge.getPrize());
            existingChallenge.setRelatedLinks(newChallenge.getRelatedLinks());
            // Update other properties as needed

            return challengesRepository.save(existingChallenge);
        } else {
            // Handle scenario where challenge with given ID is not found
        	throw new CustomException(id + " is not valid challenge id");
        }
    }

    
    
    //helpers
    static Specification<Challenges> withHasLanguage(String hasLanguage) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("language"), hasLanguage);
    }

    static Specification<Challenges> withTopicContaining(String topic) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("topics"), "%" + topic + "%");
    }

    public Challenges getChallengeById(Long id) {
        Optional<Challenges> optionalChallenge = challengesRepository.findById(id);
        return optionalChallenge.orElse(null);
    }
}
