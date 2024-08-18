# Data Pipeline Project

## Overview
This project is a data pipeline application that uses Spring Boot and Apache Kafka to process and send data. It reads data from a file and sends it to a Kafka topic.

## Technologies Used
- Java 17
- Spring Boot 3.2.8
- Apache Kafka
- Zookeeper
- Docker
- Maven

## Getting Started
- Clone the repository:  <pre>git clone git@github.com:HoHaiNam-Dev/MoMo_Test.git</pre>

## Project Structure
- `pom.xml`: Maven configuration file.
- `src/main/resources/application.yml`: Contains the application properties.
- `src/main/java/com/momo_homework/data_pipeline/DataPipelineApplication.java`: Main class for running the application.
- `src/main/java/com/momo_homework/data_pipeline/config/`: Contains the configuration classes.
- `src/main/java/com/momo_homework/data_pipeline/controller/DataPipelineController.java`: Contains the REST controller for sending data.
- `src/main/java/com/momo_homework/data_pipeline/service/datapipeline/`: Contains the service classes for processing and sending data.
- `src/main/java/com/momo_homework/data_pipeline/service/file/`: Contains the service classes for reading data from a file.
- `src/main/java/com/momo_homework/data_pipeline/service/kafka`: Contains the service classes for sending data to a Kafka topic.
- `src/main/java/com/momo_homework/data_pipeline/domain/`: Contains the domain classes.
- `src/main/java/com/momo_homework/data_pipeline/utils/enums/`: Contains the enums.
- `src/main/java/com/momo_homework/data_pipeline/utils/errors/`: Contains the handling errors classes.
- `Dockerfile`: Contains the Docker commands to build application into a Docker image.
- `docker-compose.yml`: Contains the Docker Compose configuration to run the application and Kafka.

## Prerequisites
- Java 17
- Maven
- Apache Kafka
- Docker (Optional)

## Running the Application
### Option 1: Run the application using the command line.
1. Navigate to the project directory.
2. Build the project using Maven: <pre>mvn clean install</pre>
3. Run the application: <pre>mvn spring-boot:run</pre>

### Option 2: Run the application using an IDE.
1. Import the project into your IDE.
2. Run the `DataPipelineApplication` class.

### Option 3: Run the application using docker-compose.
1. Navigate to the project directory.
2. Make sure you have Docker and Docker Compose installed. 
3. Run the following command: <pre>docker-compose up -d</pre>



## Design Decisions
1. File Processing
   Reasoning: The data processing logic was designed to handle large files efficiently without loading the entire file into memory. This decision was made to ensure scalability and prevent memory overflow when dealing with large datasets.
   Implementation:
   Buffered Reading: The application uses BufferedReader to read the file line by line. This approach allows the application to process large files in a streaming fashion, keeping memory usage low.
   Dynamic File Path Handling: The file paths are handled dynamically, allowing the application to process multiple files provided as a list in the request. This flexibility is crucial for handling various file inputs without hardcoding file names or paths.
    
2. Integration with Apache Kafka
   Reasoning: Apache Kafka was chosen as the message broker due to its scalability, fault tolerance, and high throughput, making it ideal for handling large volumes of data in a distributed system.
   Implementation:
   Kafka Producer: The application includes a Kafka producer service that sends processed data to a specified Kafka topic. Each line from the file is sent as a separate message, ensuring that the data is streamed to Kafka in real-time.
   Retry Mechanism: To handle potential connectivity issues with Kafka, a retry mechanism is implemented using Spring’s @Retryable annotation. This ensures that the application can recover from transient failures without losing data.

3. Optimizations
   Memory Usage:
   Minimizing Object Creation: The application minimizes unnecessary object creation within loops to optimize memory usage. For instance, variables are reused, and only essential data is retained in memory.
   Avoiding Full File Load: By processing the file line by line, the application avoids loading the entire file into memory, which is particularly important when dealing with files that are hundreds of megabytes or even gigabytes in size.
   Dockerization:
   Multi-stage Dockerfile: The Dockerfile is designed with a multi-stage build process. The first stage builds the application using Maven, while the second stage creates a lightweight runtime image using Alpine Linux. This results in smaller and more efficient Docker images.
   Docker Compose Integration: Docker Compose is used to orchestrate the application along with Kafka and Zookeeper. This setup simplifies deployment and ensures that all necessary services are up and running with minimal manual intervention.