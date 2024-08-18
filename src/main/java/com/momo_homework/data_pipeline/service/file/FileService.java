package com.momo_homework.data_pipeline.service.file;

import com.momo_homework.data_pipeline.utils.enums.FileType;

import java.io.File;

public interface FileService {

    /**
     * Get file from path
     *
     * @param filePath - File path
     * @param fileType - File type
     * @return File
     */
    File getFileFromPath(String filePath, FileType fileType);
}
