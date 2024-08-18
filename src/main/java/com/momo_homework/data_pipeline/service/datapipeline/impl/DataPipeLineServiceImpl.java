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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataPipeLineServiceImpl implements DataPipeLineService {
    private final FileService fileService;
    private final KafkaService kafkaService;

    private static final int MAX_CHUNK_SIZE = 1000; // Define the batch size
    private static final int FILE_PROCESSING_THREAD_COUNT = 2; // Number of threads for file processing
    private static final int FILE_CHUNK_PROCESSING_THREAD_COUNT = 4; // Number of threads for chunk processing

    @Override
    public void pushDataToKafka(List<String> filePaths) {
        ExecutorService fileProcessingExecutor = Executors.newFixedThreadPool(FILE_PROCESSING_THREAD_COUNT);
        List<Future<Void>> futures = new ArrayList<>();

        for (String filePath : filePaths) {
            // Submit each file processing task to the executor service
            Future<Void> future = fileProcessingExecutor.submit(() -> {
                try {
                    _processFileData(filePath);
                } catch (Exception e) {
                    log.error("Error processing file: {}", filePath, e);
                }
                return null;
            });
            futures.add(future);
        }

        // Wait for all file processing tasks to complete
        for (Future<Void> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                log.error("File processing was interrupted", e);
                Thread.currentThread().interrupt(); // Restore the interrupted status
            } catch (ExecutionException e) {
                log.error("Execution error during file processing", e);
            }
        }

        // Shutdown the executor service
        fileProcessingExecutor.shutdown();
    }

    private void _processFileData(String filePath) throws IOException {
        ExecutorService chunkProcessingExecutor = Executors.newFixedThreadPool(FILE_CHUNK_PROCESSING_THREAD_COUNT);
        File file = fileService.getFileFromPath(filePath, FileType.CSV);

        try (BufferedReader brd = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            List<Future<Void>> chunkFutures = new ArrayList<>();
            List<String> chunk = new ArrayList<>();
            String line;

            while ((line = brd.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    chunk.add(line);

                    // Send the chunk to the message queue if it reaches the batch size
                    if (chunk.size() >= MAX_CHUNK_SIZE) {
                        _sendToMessageQueue(chunkProcessingExecutor, chunk, chunkFutures);
                    }
                }
            }

            // Send the remaining chunk to the message queue
            if (!chunk.isEmpty()) {
                _sendToMessageQueue(chunkProcessingExecutor, chunk, chunkFutures);
            }

            // Wait for all chunk processing tasks to complete
            for (Future<Void> future : chunkFutures) {
                try {
                    future.get();
                } catch (InterruptedException e) {
                    log.error("Chunk processing was interrupted", e);
                    Thread.currentThread().interrupt(); // Restore the interrupted status
                } catch (ExecutionException e) {
                    log.error("Execution error during chunk processing", e);
                }
            }

        } catch (IOException e) {
            log.error("I/O error while processing file: {}", filePath, e);
            throw e; // Re-throw to allow the outer catch to handle
        } catch (Exception e) {
            log.error("Unexpected error while processing file: {}", filePath, e);
        } finally {
            chunkProcessingExecutor.shutdown();
        }
    }

    private void _sendToMessageQueue(ExecutorService chunkProcessingExecutor, List<String> chunk, List<Future<Void>> chunkFutures) {
        // Submit the chunk for processing
        Future<Void> chunkFuture = chunkProcessingExecutor.submit(new ChunkProcessor(chunk));

        // Add the future to the list for tracking completion
        chunkFutures.add(chunkFuture);

        // Clear the list for the next chunk
        chunk.clear();
    }

    private class ChunkProcessor implements Callable<Void> {
        private final List<String> chunk;

        public ChunkProcessor(List<String> chunk) {
            this.chunk = chunk;
        }

        @Override
        public Void call() {
            try {
                kafkaService.sendBatch(chunk);
            } catch (Exception e) {
                log.error("Error sending chunk to Kafka", e);
            }
            return null;
        }
    }
}
