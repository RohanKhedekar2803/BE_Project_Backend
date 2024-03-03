package com.example.BE_PROJECT_OPEN_COLLAB.CosineSimilarityAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class Demo {

    public List<Integer> sortRepositories(String[] userLanguages, String[] userTopics) {
        // Sample repository data
        List<List<String>> documents = new ArrayList<>();
        documents.add(Arrays.asList("JavaScript", "Java", "Machine Learning", "Artificial Intelligence", "Cloud", "Database", "SQL"));
        documents.add(Arrays.asList("Python", "Machine Learning", "Deep Learning", "Cloud", "Database"));
        documents.add(Arrays.asList("Java", "Cloud", "SQL", "Postgres"));
        documents.add(Arrays.asList("Java", "SQL", "Postgres"));
        // Add more repositories as needed

        // Compute TF-IDF vectors for user's languages and topics
        List<String> userDocuments = new ArrayList<>(Arrays.asList(userLanguages));
        userDocuments.addAll(Arrays.asList(userTopics));
        Map<String, Double> idfValues = new HashMap<>();
        List<Map<String, Double>> tfidfVectors = new ArrayList<>();
        computeTFIDFVectors(documents, idfValues, tfidfVectors);

        List<Integer> repositoryIndices = new ArrayList<>();
        for (int i = 0; i < documents.size(); i++) {
            repositoryIndices.add(i);
        }
        repositoryIndices.sort((repo1, repo2) -> {
            Map<String, Double> tfidfVector1 = tfidfVectors.get(repo1);
            Map<String, Double> tfidfVector2 = tfidfVectors.get(repo2);
            double sum1 = calculateSum(tfidfVector1, userDocuments);
            double sum2 = calculateSum(tfidfVector2, userDocuments);
            return Double.compare(sum2, sum1); // Sort in descending order of TF-IDF sums
        });

        // Print repositories in sorted order
        System.out.println("Repositories in Sorted Order:");
        for (int index : repositoryIndices) {
            System.out.println("Repository " + (index + 1) + ": " + documents.get(index));
            System.out.println("TF-IDF Vector: " + tfidfVectors.get(index));
        }

        return repositoryIndices;
    }

    private double calculateSum(Map<String, Double> tfidfVector, List<String> userDocuments) {
        double sum = 0.0;
        for (String term : userDocuments) {
            sum += tfidfVector.getOrDefault(term, 0.0);
        }
        return sum;
    }

    private void computeTFIDFVectors(List<List<String>> documents, Map<String, Double> idfValues, List<Map<String, Double>> tfidfVectors) {
        int totalDocuments = documents.size();

        // Count document frequency for each term
        Map<String, Integer> documentFrequency = new HashMap<>();
        for (List<String> document : documents) {
            Set<String> uniqueTerms = new HashSet<>(document);
            for (String term : uniqueTerms) {
                documentFrequency.put(term, documentFrequency.getOrDefault(term, 0) + 1);
            }
        }

        // Compute IDF values
        for (String term : documentFrequency.keySet()) {
            double idf = Math.log((double) totalDocuments / (double) (1 + documentFrequency.get(term))) + 1; // Add 1 to ensure non-zero value
            idfValues.put(term, idf);
        }

        // Compute TF-IDF vectors for each document
        for (List<String> document : documents) {
            Map<String, Double> tfidfVector = new HashMap<>();
            Map<String, Integer> termFrequency = new HashMap<>();
            int totalTermsInDocument = document.size();

            // Count term frequency for each term in the document
            for (String term : document) {
                termFrequency.put(term, termFrequency.getOrDefault(term, 0) + 1);
            }

            // Compute TF-IDF values for each term
            for (String term : termFrequency.keySet()) {
                double tf = (double) termFrequency.get(term) / (double) totalTermsInDocument;
                double tfidf = tf * idfValues.get(term);
                tfidfVector.put(term, tfidf);
            }

            tfidfVectors.add(tfidfVector);
        }
    }
}
