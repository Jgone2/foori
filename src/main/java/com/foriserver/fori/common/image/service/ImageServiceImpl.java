package com.foriserver.fori.common.image.service;

import com.foriserver.fori.common.exception.CodeEnum.ExceptionCode;
import com.foriserver.fori.common.image.entity.Image;
import com.foriserver.fori.common.image.repository.ImageRepository;
import com.foriserver.fori.common.s3.service.S3ServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

//    private final S3ServiceImpl s3Service;
    private final ImageRepository imageRepository;
    @Override
    public Image findImage(String imageFilePath) {
        Image verifiedImage = findVerifiedImage(imageFilePath);
        return verifiedImage;
    }

    @Override
    public List<Image> findImages() {
        return null;
    }

    @Override
    public void deleteImage(String imageFilePath) {

    }

    @Override
    public Image findVerifiedImage(String imageFilePath) {
        Image findImage = imageRepository.findByFilePath(imageFilePath);
        if (findImage == null) {    // 이미지가 없으면
            throw new IllegalArgumentException(ExceptionCode.IMAGE_NOT_FOUND.getMessage());
        }
        return findImage;
    }

    // 이미지 리사이징(사용자 프로필 이미지)

}
