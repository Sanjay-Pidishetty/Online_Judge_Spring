# Use a base image with JDK 8 (since your project uses Java 1.8)
FROM openjdk:8-jdk-alpine

# Set the working directory inside the container
WORKDIR /app/backend

#RUN apk update && apk add --no-cache openjdk11 bash curl
RUN apk add --update build-base

# Copy the Spring Boot JAR file from your local machine to the container
# This assumes your JAR file is in the target directory after building the project with Maven
COPY target/Online_judge.jar /app/backend

# Expose the port that your Spring Boot application will run on
EXPOSE 8080

# Run the JAR file using Java
ENTRYPOINT ["java", "-jar", "/app/backend/Online_judge.jar"]