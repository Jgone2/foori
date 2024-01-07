package com.foriserver.fori.common.s3.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface S3Service {

    //  파일 업로드
    String uploadFile(MultipartFile multipartFile, String dirName);
    List<String> uploadFiles(List<MultipartFile> multipartFiles, String dirName);

    // S3에 파일 업로드
    String putS3(MultipartFile multipartFile, String dirName);

    // 사전 서명된 URL 생성 (업로드된 파일의 URL을 얻는다.)
    String generatePresignedUrl(String fileName);

    // 파일 삭제
    void deleteFileFromS3(String fileName);
}
