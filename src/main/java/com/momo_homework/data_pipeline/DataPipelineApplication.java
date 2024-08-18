package com.momo_homework.data_pipeline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

@SpringBootApplication
public class DataPipelineApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataPipelineApplication.class, args);

        // Uncomment the following line to generate a large CSV file for testing
//        _generateTestingFile();
    }

    // Helper method to generate a large CSV file for testing
    private static void _generateTestingFile() {
        String filePath = "test-data/LargeUserSegments_1GB.csv";

        long targetFileSizeBytes = 1L * 1024L * 1024L * 1024L; // 10GB
        int bytesPerRow = 100; // Estimate: Adjust based on the actual row size
        long currentFileSizeBytes = 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            Random random = new Random();

            while (currentFileSizeBytes < targetFileSizeBytes) {
                // Generate random user_id and segment_type
                String userId = "user" + random.nextInt(100000000);
                String segmentType = "segment" + random.nextInt(1000);

                // Create a CSV row
                String row = userId + "," + segmentType + "\n";

                // Write the row to the file
                writer.write(row);

                // Update the current file size
                currentFileSizeBytes += row.getBytes().length;
            }

            System.out.println("1GB CSV file generated: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

}
