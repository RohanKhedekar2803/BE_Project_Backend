package com.example.BE_PROJECT_OPEN_COLLAB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class trashService {
	
	private double cosineSimilarity(Map<String, Double> userTFIDF, Map<String, Double> jobTFIDF) {
        double dotProduct = 0.0;
        double magnitudeVec1 = 0.0;
        double magnitudeVec2 = 0.0;

        // Compute dot product and magnitudes of both vectors
        for (Entry<String, Double> entry : userTFIDF.entrySet()) {
            String term = entry.getKey();
            Double freqVec1 = entry.getValue();
            Double freqVec2 = jobTFIDF.getOrDefault(term, 0.0);

            dotProduct += freqVec1 * freqVec2;
            magnitudeVec1 += Math.pow(freqVec1, 2);
        }

        for (Double freqVec2 : jobTFIDF.values()) {
            magnitudeVec2 += Math.pow(freqVec2, 2);
        }

        // Compute cosine similarity
        if (magnitudeVec1 == 0 || magnitudeVec2 == 0) {
            return 0.0;
        } else {
            return dotProduct / (Math.sqrt(magnitudeVec1) * Math.sqrt(magnitudeVec2));
        }
    }

    // Method to calculate TF-IDF vector for a given document
    private Map<String, Double> calculateTFIDFVector(List<String> document, List<List<String>> documents) {
        Map<String, Integer> termFrequency = new HashMap<>();
        Map<String, Double> tfidfVector = new HashMap<>();
        int totalDocuments = documents.size();

        // Count term frequency for each term in the document
        for (String term : document) {
            termFrequency.put(term, termFrequency.getOrDefault(term, 0) + 1);
        }

        // Compute TF-IDF values for each term
        for (String term : document) {
            double tf = (double) termFrequency.get(term) / document.size();
            double idf = calculateIDF(term, documents);
            double tfidf = tf * idf;
            tfidfVector.put(term, tfidf);
        }

        return tfidfVector;
    }

    // Method to calculate IDF value for a given term
    private double calculateIDF(String term, List<List<String>> documents) {
        int documentFrequency = 0;

        for (List<String> document : documents) {
            if (document.contains(term)) {
                documentFrequency++;
            }
        }

        return Math.log((double) documents.size() / (double) (1 + documentFrequency));
    }

    // Main method to sort repositories based on matching user skills with job requirements
    public List<Integer> sortRepositories(List<String> userSkills, List<List<String>> jobRequirements) {
        List<Map<String, Double>> tfidfVectors = new ArrayList<>();

        // Use thread pool for parallel computation
        int numThreads = Runtime.getRuntime().availableProcessors(); // Get the number of available processors
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        try {
            // Submit tasks to the thread pool for TF-IDF calculation
            for (List<String> job : jobRequirements) {
                executor.submit(() -> {
                    Map<String, Double> tfidfVector = calculateTFIDFVector(job, jobRequirements);
                    synchronized (tfidfVectors) {
                        tfidfVectors.add(tfidfVector);
                    }
                });
            }
        } finally {
            // Shutdown the executor once all tasks are submitted
            executor.shutdown();
            try {
                // Wait for all tasks to complete
                executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                // Handle interruption
                Thread.currentThread().interrupt();
            }
        }

        // Calculate TF-IDF vector for user skills
        Map<String, Double> userTFIDF = calculateTFIDFVector(userSkills, jobRequirements);

        // Compute cosine similarity between user skills and each job requirement
        List<Double> similarities = new ArrayList<>();
        for (Map<String, Double> jobTFIDF : tfidfVectors) {
            similarities.add(cosineSimilarity(userTFIDF, jobTFIDF));
        }

        // Sort repositories based on cosine similarity in descending order
        List<Integer> repositoryIndices = new ArrayList<>();
        for (int i = 0; i < jobRequirements.size(); i++) {
            repositoryIndices.add(i);
        }
        repositoryIndices.sort((repo1, repo2) -> Double.compare(similarities.get(repo2), similarities.get(repo1)));

        return repositoryIndices;
    }
}
