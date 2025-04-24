FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy your JAR from target folder to app folder
COPY target/back-0.0.1-SNAPSHOT.jar app.jar


# Expose the port your app runs on (usually 8080)
EXPOSE 8081

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]