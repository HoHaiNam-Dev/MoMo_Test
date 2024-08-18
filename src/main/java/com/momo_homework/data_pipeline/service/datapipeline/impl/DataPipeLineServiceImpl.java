package com.momo_homework.data_pipeline.service.datapipeline.impl;

import com.momo_homework.data_pipeline.service.datapipeline.DataPipeLineService;
import com.momo_homework.data_pipeline.service.file.FileService;
import com.momo_homework.data_pipeline.service.kafka.KafkaService;
import com.momo_homework.data_pipeline.utils.enums.FileType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataPipeLineServiceImpl implements DataPipeLineService {
    private final FileService fileService;
    private final KafkaService kafkaService;
    private static final int BATCH_SIZE = 100; // Define the batch size

    @Override
    public void pushDataToKafka(List<String> filePaths) {
        for (String filePath : filePaths) {
            log.info("Starting to process file: {}", filePath);

            _processFileData(filePath);

            log.info("Finished processing file: {}", filePath);

        }
    }


    private void _processFileData(String filePath) {
        File file = fileService.getFileFromPath(filePath, FileType.CSV);
        List<String> batch = new ArrayList<>();
        try (BufferedReader brd = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = brd.readLine()) != null) {
                // Skip empty lines
                if (!line.trim().isEmpty()) {
                    batch.add(line);
                    // If batch size is reached, send the batch to Kafka
                    if (batch.size() >= BATCH_SIZE) {
                        kafkaService.sendBatch(batch);
                        batch.clear(); // Clear the batch after sending
                    }
                }
            }

            // Send any remaining lines that didn't fill up the last batch
            if (!batch.isEmpty()) {
                kafkaService.sendBatch(batch);
            }

        } catch (IOException e) {
            log.error("I/O error while processing file: {}", filePath, e);
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("ArrayIndexOutOfBoundsException: Malformed line in file: {}", filePath, e);
        } catch (NullPointerException e) {
            log.error("NullPointerException: Unexpected null value encountered: {}", filePath, e);
        } catch (Exception e) {
            log.error("Unexpected error while processing file: {}", filePath, e);
        }
    }

}
