package com.momo_homework.data_pipeline.utils.enums;

import lombok.Getter;

@Getter
public enum FileType {
    BMP("bmp"),

    JPG("jpg"),

    PNG("png"),

    GIF("gif"),

    ICO("ico"),

    SVG("svg"),

    TIFF("tiff"),

    WEBP("webp"),

    AVI("avi"),

    FLV("flv"),

    MKV("mkv"),

    MOV("mov"),

    MP4("mp4"),

    MPG("mpg"),

    WMV("wmv"),

    MP3("mp3"),

    WAV("wav"),

    WMA("wma"),

    AAC("aac"),

    FLAC("flac"),

    OGG("ogg"),

    PDF("pdf"),

    DOC("doc"),

    DOCX("docx"),

    XLS("xls"),

    XLSX("xlsx"),

    POWERPOINT("ppt"),

    CSV("csv");

    private final String extension;

    FileType( final String extension) {
        this.extension = extension;
    }

}
