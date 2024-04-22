package com.foriserver.fori.common.s3.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.foriserver.fori.common.exception.CodeEnum.ExceptionCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.amazonaws.HttpMethod.GET;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String uploadFile(MultipartFile multipartFile, String dirName) {
        return putS3(multipartFile, dirName);
    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> multipartFiles, String dirName) {
        ArrayList<String> uploadImgUrlList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String uploadImgUrl = putS3(multipartFile, dirName);    // S3에 파일 업로드하고 URL을 가져옴
            uploadImgUrlList.add(uploadImgUrl);   // URL을 리스트에 추가
        }
        return uploadImgUrlList;    // 파일 URL 리스트 반환
    }

    @Override
    public String putS3(MultipartFile multipartFile, String dirName) {
        // 파일 생성 시간을 이용해서 파일 이름을 생성
        String fileName = setFileName(multipartFile, dirName);

        // MetaData 생성
        ObjectMetadata metadata = setMetaData(multipartFile);

        // S3에 업로드
        putObject(multipartFile, fileName, metadata);

        return fileName;
    }

    @Override
    public String generatePresignedUrl(String fileName) {
        // S3에 업로드된 파일의 URL을 얻는다.
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60;    // URL 만료 시간은 1시간
        expiration.setTime(expTimeMillis);

        // 사전 서명된 URL 생성
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, fileName)
                .withMethod(GET)
                .withExpiration(expiration);

        URL url = amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    @Override
    public void deleteFileFromS3(String fileName) {
        amazonS3Client.deleteObject(bucket, fileName);
        log.info("파일이 S3에서 삭제되었습니다: {}", fileName);
    }

    private void putObject(MultipartFile multipartFile, String fileName, ObjectMetadata metadata) {
        try {
            // S3에 파일내 업로드. 파일의 내용, 이름, 메타데이터를 전달
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));    // PublicRead 권한으로 업로드
        } catch (Exception e) {
            log.error("파일 업로드 실패", e);
            throw new RuntimeException(ExceptionCode.FILE_UPLOAD_FAIL.getMessage(), e);
        }
        log.info("파일이 S3에 업로드되었습니다: {}", fileName);
    }

    private static ObjectMetadata setMetaData(MultipartFile multipartFile) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(multipartFile.getContentType());    // 파일 타입 설정
        metadata.setContentLength(multipartFile.getSize());        // 파일 크기 설정
        return metadata;    // 메타데이터 반환
    }

    private static String setFileName(MultipartFile multipartFile, String dirName) {
        // 파일 생성시간을 이용해 파일이름을 생성
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String createdTime = now.format(timeFormatter);

        // UUID값 앞자리 8자리 생성(파일명 중복방지 차원)
        String shortHashUuid = getShortHashUUID();

        StringBuilder fileNameBuilder = new StringBuilder();
        String originalFileName = multipartFile.getOriginalFilename();
        fileNameBuilder.append(dirName).append("/");

        try {
            if (originalFileName != null) {
                fileNameBuilder.append(createdTime).append("_").append(originalFileName).append("_").append(shortHashUuid);
            }
        } catch (Exception e) {
            log.error("파일 이름 생성 실패", e);
            throw new RuntimeException(ExceptionCode.FILE_NAME_GENERATE_FAIL.getMessage(), e);
        }

        String fileName = fileNameBuilder.toString();    // 파일 이름 반환
        log.info("파일 이름: {}", fileName);
        return fileName;
    }

    private static String getShortHashUUID() {
        UUID uuid = UUID.randomUUID();
        return new StringBuilder(uuid.toString()).substring(0, 8);
    }
}
