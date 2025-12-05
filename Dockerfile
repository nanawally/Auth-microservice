# Use an official lightweight OpenJDK 21 image
FROM eclipse-temurin:21-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR into the container
# IMPORTANT: Make sure the JAR name matches the file inside build/libs/
COPY build/libs/Auth-microservice-0.0.1-SNAPSHOT.jar /app/auth.jar

# Expose the port the microservice runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/auth.jar"]

