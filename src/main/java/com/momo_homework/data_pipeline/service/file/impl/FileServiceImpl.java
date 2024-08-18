package com.momo_homework.data_pipeline.service.file.impl;

import com.momo_homework.data_pipeline.service.file.FileService;
import com.momo_homework.data_pipeline.utils.enums.FileType;
import com.momo_homework.data_pipeline.utils.error.dtos.ErrorMessage;
import com.momo_homework.data_pipeline.utils.error.exception.FileUploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    /**
     * Get file from path
     *
     * @param filePath - path to file
     * @param fileType - type of file that is expected
     * @return File
     */
    @Override
    public File getFileFromPath(String filePath, FileType fileType) {
        File file = new File(filePath);

        validateFile(file, fileType);

        return file;
    }

    private static void validateFile(File file, FileType fileType) {
        isValidPath(file.getPath());
        isFileExists(file);
        isFileEmpty(file);
        isCorrectFileType(file, fileType);
    }

    private static void isFileEmpty(File file) {
        if (file.length() == 0) {
            log.error("File is empty");
            throw new FileUploadException(ErrorMessage.FILE_IS_EMPTY);
        }
    }

    private static void isFileExists(File file) {
        if (!file.exists()) {
            log.error("File does not exist");
            throw new FileUploadException(ErrorMessage.FILE_DOES_NOT_EXIST);
        }
    }

    private static void isCorrectFileType(File file, FileType fileType) {
        if (!file.getName().endsWith(fileType.getExtension())) {
            log.error("File type must be: {}", fileType.getExtension());
            throw new FileUploadException(ErrorMessage.FILE_TYPE_INCORRECT);
        }
    }

    private static void isValidPath(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException | NullPointerException ex) {
            log.error("File path is invalid");
            throw new FileUploadException(ErrorMessage.FILE_PATH_INVALID);
        }
    }

}