# ===== Stage 1: Build the application =====
FROM gradle:8.7-jdk21 AS builder
WORKDIR /app

# Copy everything and build the JAR
COPY . .
RUN gradle build -x test

# ===== Stage 2: Run the application =====
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/build/libs/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
