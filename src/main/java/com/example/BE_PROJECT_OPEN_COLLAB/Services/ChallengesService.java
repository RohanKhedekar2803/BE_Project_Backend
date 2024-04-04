package com.example.BE_PROJECT_OPEN_COLLAB.Services;

import java.util.ArrayList;
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
import com.example.BE_PROJECT_OPEN_COLLAB.CosineSimilarityAlgorithm.CosineSimilarity;
import com.example.BE_PROJECT_OPEN_COLLAB.Entity.Challenges;
import com.example.BE_PROJECT_OPEN_COLLAB.Repositories.ChallengesRepository;
import com.example.BE_PROJECT_OPEN_COLLAB.Utilities.FilterRepos;

@Service
public class ChallengesService {

    @Autowired
    private ChallengesRepository challengesRepository;
    
    @Autowired
    private CosineSimilarity demo;

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
    
    //......................................................................................
    
    //new ml model 
    public List<Challenges> getChallengesByProfile(Integer pageNo, Integer pageSize, String sortBy, FilterRepos filterChallenges, String[] userLanguages, String[] userTopics, String username) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, sortBy));
        Specification<Challenges> spec = Specification.where(null);

        // Fetch all challenges from the database
        List<Challenges> allChallenges = challengesRepository.findAll(spec);

        // Apply filtering if provided
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

        // Filter challenges based on the provided criteria
        List<Challenges> filteredChallenges = challengesRepository.findAll(spec);

        // Check if custom sorting is applied
        if ("recommended".equals(sortBy)) {
            System.out.println("in recommendation");
            // Extract languages and topics from the fetched challenges
            List<List<String>> documents = extractLanguagesAndTopics(allChallenges);

            // Sort challenges using TF-IDF algorithm based on user languages and topics
            List<Integer> sortedIndices = demo.sortRepositoriesByCosineSimilarity(userLanguages, userTopics, documents);

            // Map the sorted indices to the list of challenges
            List<Challenges> sortedChallenges = new ArrayList<>();
            for (int index : sortedIndices) {
                sortedChallenges.add(allChallenges.get(index));
            }

            // Apply filtering on the custom sorted data
            sortedChallenges = applyFiltering(sortedChallenges, filterChallenges);

            // Perform pagination on the sorted and filtered challenges
            return paginate(sortedChallenges, pageNo, pageSize);
        } else {
            System.out.println("not in recommendation");
            // Perform pagination on the filtered challenges
            return paginate(filteredChallenges, pageNo, pageSize);
        }
    }
    
    //helper for ml 
    

    private List<Challenges> paginate(List<Challenges> challenges, int pageNo, int pageSize) {
        int start = pageNo * pageSize;
        int end = Math.min(start + pageSize, challenges.size());
        return challenges.subList(start, end);
    }
    
    private List<Challenges> applyFiltering(List<Challenges> challenges, FilterRepos filterChallenges) {
        List<Challenges> filteredChallenges = new ArrayList<>(challenges);
        // Apply filtering logic here based on filterChallenges
        // For example:
        System.out.println(filterChallenges.getHasLanguage());
        if (filterChallenges.getHasLanguage().length() > 0) {
            System.out.println("in lang" + filteredChallenges.size());
            filteredChallenges.removeIf(challenge -> challenge.getLanguage().equals(filterChallenges.getHasLanguage()));
            System.out.println("in lang" + filteredChallenges.size());
        } 
        if (!filterChallenges.getHasTopic().isEmpty()) {
            System.out.println("in topic" + filteredChallenges.size());
            filteredChallenges.removeIf(challenge -> !challenge.getTopics().contains(filterChallenges.getHasTopic()));
            System.out.println("in topic" + filteredChallenges.size());
        }
        return filteredChallenges;
    }

    private List<List<String>> extractLanguagesAndTopics(List<Challenges> challenges) {
        List<List<String>> documents = new ArrayList<>();
        for (Challenges challenge : challenges) {
            List<String> challengeData = new ArrayList<>();
            // Add languages and topics from the challenge to the document
            challengeData.add(challenge.getLanguage());
            challengeData.add(challenge.getTopics());
            documents.add(challengeData);
        }
        return documents;
    }

	public List<Challenges> getChallengescreatedBy(String username) {
		List<Challenges> list =challengesRepository.findByCreatedBy(username);
		return list;
	}
}
