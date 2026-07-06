package com.vivagoa.service;

import com.vivagoa.entity.GalleryImage;
import com.vivagoa.repository.GalleryImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class GalleryServiceTest {

    @Mock
    private GalleryImageRepository galleryImageRepository;

    @InjectMocks
    private GalleryService galleryService;

    private GalleryImage image;

    @BeforeEach
    void setUp() {
        image = new GalleryImage();
        image.setId(1L);
        image.setFileName("goan-fish-curry.jpg");
        image.setTitle("Goan Fish Curry");
        image.setDescription("Traditional Goan fish curry");
        image.setCategory(GalleryImage.Category.FOOD);
        image.setDisplayOrder(1);
        image.setActive(true);
    }

    @Test
    void save_shouldSaveAndReturnImage() {
        when(galleryImageRepository.save(any(GalleryImage.class))).thenReturn(image);

        GalleryImage saved = galleryService.save(image);

        assertThat(saved).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Goan Fish Curry");
        assertThat(saved.getFileName()).isEqualTo("goan-fish-curry.jpg");
        verify(galleryImageRepository, times(1)).save(image);
    }

    @Test
    void findById_shouldReturnImageWhenExists() {
        when(galleryImageRepository.findById(1L)).thenReturn(Optional.of(image));

        Optional<GalleryImage> result = galleryService.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Goan Fish Curry");
    }

    @Test
    void findById_shouldReturnEmptyWhenNotExists() {
        when(galleryImageRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<GalleryImage> result = galleryService.findById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void findAll_shouldReturnAllImages() {
        GalleryImage image2 = new GalleryImage();
        image2.setId(2L);
        image2.setFileName("restaurant-interior.jpg");
        image2.setTitle("Restaurant Interior");
        image2.setCategory(GalleryImage.Category.AMBIANCE);
        image2.setActive(true);

        when(galleryImageRepository.findAll()).thenReturn(Arrays.asList(image, image2));

        List<GalleryImage> result = galleryService.findAll();

        assertThat(result).hasSize(2);
        verify(galleryImageRepository).findAll();
    }

    @Test
    void findActiveImages_shouldReturnOnlyActiveImages() {
        when(galleryImageRepository.findByActiveTrueOrderByDisplayOrder())
                .thenReturn(Collections.singletonList(image));

        List<GalleryImage> result = galleryService.findActiveImages();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).isActive()).isTrue();
        verify(galleryImageRepository).findByActiveTrueOrderByDisplayOrder();
    }

    @Test
    void findByCategory_shouldReturnImagesForCategory() {
        when(galleryImageRepository.findByCategory(GalleryImage.Category.FOOD))
                .thenReturn(Collections.singletonList(image));

        List<GalleryImage> result = galleryService.findByCategory(GalleryImage.Category.FOOD);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategory()).isEqualTo(GalleryImage.Category.FOOD);
        verify(galleryImageRepository).findByCategory(GalleryImage.Category.FOOD);
    }

    @Test
    void findByCategory_shouldReturnEmptyForNoMatch() {
        when(galleryImageRepository.findByCategory(GalleryImage.Category.EVENTS))
                .thenReturn(Collections.emptyList());

        List<GalleryImage> result = galleryService.findByCategory(GalleryImage.Category.EVENTS);

        assertThat(result).isEmpty();
    }

    @Test
    void toggleActive_shouldFlipActiveFlag() {
        assertThat(image.isActive()).isTrue();
        when(galleryImageRepository.findById(1L)).thenReturn(Optional.of(image));
        when(galleryImageRepository.save(any(GalleryImage.class))).thenReturn(image);

        GalleryImage toggled = galleryService.toggleActive(1L);

        assertThat(toggled.isActive()).isFalse();
        verify(galleryImageRepository).findById(1L);
        verify(galleryImageRepository).save(image);
    }

    @Test
    void toggleActive_shouldThrowExceptionWhenNotFound() {
        when(galleryImageRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> galleryService.toggleActive(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Gallery image not found");
    }

    @Test
    void delete_shouldCallDeleteById() {
        doNothing().when(galleryImageRepository).deleteById(1L);

        galleryService.delete(1L);

        verify(galleryImageRepository, times(1)).deleteById(1L);
    }
}
