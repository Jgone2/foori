package com.foriserver.fori.common.image.repository;

import com.foriserver.fori.common.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByFilePath(String path);
}
