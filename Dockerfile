# First stage: build the application
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory for the build process
WORKDIR /app

# Copy the pom.xml file and download dependencies
COPY pom.xml .

# Download the dependencies (will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline

# Copy the entire project source
COPY src ./src

# Run the Maven clean and package commands
RUN mvn clean package -DskipTests

# Second stage: create the runtime image
FROM eclipse-temurin:17-jre-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the Spring Boot JAR file from the build stage
COPY --from=build /app/target/DATA_PIPLINE-0.0.1-SNAPSHOT.jar /app/DATA_PIPLINE-0.0.1-SNAPSHOT.jar

# Expose the port your Spring Boot application listens on (default is 8080)
EXPOSE 8081

# Run the Spring Boot application when the container starts
ENTRYPOINT ["java", "-jar", "DATA_PIPLINE-0.0.1-SNAPSHOT.jar"]
