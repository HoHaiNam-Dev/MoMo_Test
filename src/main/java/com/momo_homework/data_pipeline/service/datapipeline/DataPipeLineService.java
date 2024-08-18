package com.momo_homework.data_pipeline.service.datapipeline;

import java.util.List;

public interface DataPipeLineService {
    /**
     * Process file data and push to Kafka
     *
     * @param filePaths - List of file paths
     */
    void pushDataToKafka(List<String> filePaths);
}
