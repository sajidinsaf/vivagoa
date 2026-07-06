package com.vivagoa.service;

import com.vivagoa.entity.GalleryImage;
import com.vivagoa.repository.GalleryImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GalleryService {

    private final GalleryImageRepository galleryImageRepository;

    public GalleryService(GalleryImageRepository galleryImageRepository) {
        this.galleryImageRepository = galleryImageRepository;
    }

    public GalleryImage save(GalleryImage image) {
        return galleryImageRepository.save(image);
    }

    public Optional<GalleryImage> findById(Long id) {
        return galleryImageRepository.findById(id);
    }

    public Optional<GalleryImage> findByFileName(String fileName) {
        return galleryImageRepository.findByFileName(fileName);
    }

    public List<GalleryImage> findAll() {
        return galleryImageRepository.findAll();
    }

    public List<GalleryImage> findActiveImages() {
        return galleryImageRepository.findByActiveTrueOrderByDisplayOrder();
    }

    public List<GalleryImage> findByCategory(GalleryImage.Category category) {
        return galleryImageRepository.findByCategory(category);
    }

    public List<GalleryImage> findActiveByCategoryOrdered(GalleryImage.Category category) {
        return galleryImageRepository.findByCategoryAndActiveTrueOrderByDisplayOrder(category);
    }

    public GalleryImage toggleActive(Long id) {
        GalleryImage image = galleryImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gallery image not found with id: " + id));
        image.setActive(!image.isActive());
        return galleryImageRepository.save(image);
    }

    public void reorder(Long id, int newOrder) {
        GalleryImage image = galleryImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gallery image not found with id: " + id));
        image.setDisplayOrder(newOrder);
        galleryImageRepository.save(image);
    }

    public void delete(Long id) {
        galleryImageRepository.deleteById(id);
    }
}
