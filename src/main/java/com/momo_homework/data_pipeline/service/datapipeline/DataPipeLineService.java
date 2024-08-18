package com.momo_homework.data_pipeline.service.datapipeline;

import java.util.List;

public interface DataPipeLineService {
    void pushDataToKafka(List<String> filePaths);
}
