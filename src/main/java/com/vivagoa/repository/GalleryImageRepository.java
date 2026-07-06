package com.vivagoa.repository;

import com.vivagoa.entity.GalleryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GalleryImageRepository extends JpaRepository<GalleryImage, Long> {

    List<GalleryImage> findByActiveTrueOrderByDisplayOrder();

    List<GalleryImage> findByCategory(GalleryImage.Category category);

    List<GalleryImage> findByCategoryAndActiveTrueOrderByDisplayOrder(GalleryImage.Category category);

    Optional<GalleryImage> findByFileName(String fileName);
}
