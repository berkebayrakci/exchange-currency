# Use an official OpenJDK 17 image
FROM eclipse-temurin:17-jdk-jammy

# Set a working directory
WORKDIR /app

# Copy the jar file into the container
COPY target/task-1.0.0.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
