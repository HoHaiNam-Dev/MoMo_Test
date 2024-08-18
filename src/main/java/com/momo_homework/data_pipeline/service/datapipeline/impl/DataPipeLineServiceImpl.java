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
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataPipeLineServiceImpl implements DataPipeLineService {
    private final FileService fileService;
    private final KafkaService kafkaService;
    private static final String DL = ","; // Assuming comma is the delimiter

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

        try (BufferedReader brd = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            String[] fields;
            String userId;
            String segment;
            while ((line = brd.readLine()) != null) {
                // Skip empty lines
                if (!line.trim().isEmpty()) {
                    fields = line.split(DL);

                    if (fields.length < 2) {
                        log.info("Skipping line due to insufficient data: {}", line);
                        continue;
                    }

                    userId = fields[0];
                    segment = fields[1];

                    if (userId == null || userId.trim().isEmpty() || segment == null || segment.trim().isEmpty()) {
                        log.info("Skipping line due to null or empty userId or segment: {}", line);
                        continue;
                    }

                    kafkaService.send(userId, line);
                }
            }

        } catch (IOException e) {
            log.error("I/O error while processing file: {}", filePath, e);
        }
    }

}
