package com.example.BE_PROJECT_OPEN_COLLAB.CosineSimilarityAlgorithm;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class CosineSimilarity {

	private final String LOG_FILE_PATH = "repository_logs.txt";


	private static final String LOG_FILE_PATH_COSINE_SIMILARITY = "repository_logs.txt";

	public List<Integer> sortRepositoriesByCosineSimilarity(String[] userLanguages, String[] userTopics,
			List<List<String>> repoDocuments) {
		List<String> userDocuments = new ArrayList<>();

		// Add user topics and languages to user documents
		userDocuments.addAll(Arrays.asList(userTopics));
		userDocuments.addAll(Arrays.asList(userLanguages));

		List<Integer> repositoryIndices = new ArrayList<>();
		for (int i = 0; i < repoDocuments.size(); i++) {
			repositoryIndices.add(i);
		}

		try (FileWriter writer = new FileWriter(LOG_FILE_PATH, false)) {
			writer.write("Repositories in Sorted Order:\n");
			writer.write("User data: " + Arrays.toString(userLanguages) + " " + Arrays.toString(userTopics) + "\n\n");

			repositoryIndices.sort((repo1, repo2) -> {
				double similarity1 = computeCosineSimilarity(repoDocuments.get(repo1), userDocuments);
				double similarity2 = computeCosineSimilarity(repoDocuments.get(repo2), userDocuments);

				return Double.compare(similarity2, similarity1); // Sort in descending order of cosine similarity
			});

			// Write repositories in sorted order to a log file
			Set<Integer> printedIndices = new HashSet<>();
			for (int index : repositoryIndices) {
				if (printedIndices.add(index)) {
					writer.write("Repository ID: " + index + "\n");
					writer.write("Similarity with user: "
							+ computeCosineSimilarity(repoDocuments.get(index), userDocuments) + "\n");
					writer.write("repository is"+ repoDocuments.get(index));
				}
			}

		} catch (IOException e) {
			System.err.println("Failed to write logs to file: " + e.getMessage());
		}

		return repositoryIndices;
	}

	private double computeCosineSimilarity(List<String> document1, List<String> document2) {
		Map<String, Integer> tf1 = computeTermFrequency(document1);
		Map<String, Integer> tf2 = computeTermFrequency(document2);

		double dotProduct = 0.0;
		double magnitude1 = 0.0;
		double magnitude2 = 0.0;

		Set<String> vocabulary = new HashSet<>(tf1.keySet());
		vocabulary.addAll(tf2.keySet());

		for (String term : vocabulary) {
			int tfValue1 = tf1.getOrDefault(term, 0);
			int tfValue2 = tf2.getOrDefault(term, 0);

			dotProduct += tfValue1 * tfValue2;
			magnitude1 += tfValue1 * tfValue1;
			magnitude2 += tfValue2 * tfValue2;
		}

		magnitude1 = Math.sqrt(magnitude1);
		magnitude2 = Math.sqrt(magnitude2);

		if (magnitude1 == 0.0 || magnitude2 == 0.0) {
			return 0.0; // Prevent division by zero
		}

		return dotProduct / (magnitude1 * magnitude2);
	}

	private Map<String, Integer> computeTermFrequency(List<String> document) {
		Map<String, Integer> termFrequency = new HashMap<>();
		for (String term : document) {
			termFrequency.put(term, termFrequency.getOrDefault(term, 0) + 1);
		}
		return termFrequency;
	}

}
