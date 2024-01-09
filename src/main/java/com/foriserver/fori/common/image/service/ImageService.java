package com.foriserver.fori.common.image.service;

import com.foriserver.fori.common.image.entity.Image;

import java.util.List;

public interface ImageService  {

    Image findImage(String imageFilePath);
    List<Image> findImages();

    void deleteImage(String imageFilePath);

    Image findVerifiedImage(String imageFilePath);
}
