package com.foriserver.fori.common.image.dto;

import com.foriserver.fori.common.pattern.custom.image.FileName;
import com.foriserver.fori.common.pattern.custom.image.FilePath;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class ImageDto {

    @Getter
    @AllArgsConstructor
    public static class Post {
        @FileName
        private String fileName;
        @FilePath
        private String filePath;
    }

    @Getter
    @AllArgsConstructor
    public static class Patch {
        @FileName
        private String fileName;
        @FilePath
        private String filePath;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long id;
        @FileName
        private String fileName;
        @FilePath
        private String filePath;
    }
}
