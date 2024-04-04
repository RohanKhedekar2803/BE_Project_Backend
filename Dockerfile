# Use AdoptOpenJDK 17 as the base image
FROM openjdk:17

# Copy the compiled Spring Boot application JAR file into the container
COPY target/OpenCollab.jar OpenCollab.jar

# Expose port 8080
EXPOSE 9005

# Command to run the Spring Boot application when the container starts
CMD ["java", "-jar", "/OpenCollab.jar"]
