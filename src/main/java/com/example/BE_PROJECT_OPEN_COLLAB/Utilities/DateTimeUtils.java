package com.example.BE_PROJECT_OPEN_COLLAB.Utilities;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.BE_PROJECT_OPEN_COLLAB.Chatapp.Repositories.RepoRepository;

public class DateTimeUtils {

	@Autowired
	RepoRepository repoRepository;

	private static final ZoneId INDIA_ZONE = ZoneId.of("Asia/Kolkata");

	private DateTimeUtils() {
		// Private constructor to prevent instantiation
	}

	public static String getCurrentDateTimeInIndia() {
		// Get the current date and time in India timezone
		LocalDateTime indiaDateTime = LocalDateTime.now(INDIA_ZONE);

		// Format the date and time
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		return indiaDateTime.format(formatter);
	}

	public static String getCurrentTimeInIndia() {
		// Get the current time in India timezone
		LocalTime indiaTime = LocalTime.now(INDIA_ZONE);

		// Format the time
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

		return indiaTime.format(formatter);
	}

	public static LocalDateTime saveEntityWithDateTimeString(String dateTimeString) {
		// Parse the date-time string into a LocalDateTime object

//        String dateTimeString = "2017-11-29T17:35:03Z";
		LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME);

		return dateTime;
	}
}
