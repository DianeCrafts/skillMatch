FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy the built jar into the image
COPY target/*.jar app.jar

# Spring Boot default port
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
