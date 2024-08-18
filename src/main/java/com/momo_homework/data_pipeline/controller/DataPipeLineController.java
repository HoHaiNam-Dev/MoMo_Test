package com.momo_homework.data_pipeline.controller;

import com.momo_homework.data_pipeline.service.datapipeline.DataPipeLineService;
import com.momo_homework.data_pipeline.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pipeline")
@RequiredArgsConstructor
public class DataPipeLineController {

    private final DataPipeLineService dataPipeLineService;

    @PostMapping("/push-data")
    @ResponseStatus(HttpStatus.OK)
    public String processFiles(@RequestBody List<String> filePaths) {
        dataPipeLineService.pushDataToKafka(filePaths);
        return "File processing started successfully!";
    }
}