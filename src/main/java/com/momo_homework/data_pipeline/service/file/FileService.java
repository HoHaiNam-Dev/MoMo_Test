package com.momo_homework.data_pipeline.service.file;

import com.momo_homework.data_pipeline.utils.enums.FileType;

import java.io.File;

public interface FileService {
    File getFileFromPath(String filePath, FileType fileType);
}
