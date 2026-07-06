package com.vivagoa.controller;

import com.vivagoa.entity.GalleryImage;
import com.vivagoa.service.ContactService;
import com.vivagoa.service.GalleryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class GalleryController {

    private final GalleryService galleryService;
    private final ContactService contactService;

    public GalleryController(GalleryService galleryService, ContactService contactService) {
        this.galleryService = galleryService;
        this.contactService = contactService;
    }

    @GetMapping("/gallery")
    public String gallery(Model model) {
        List<GalleryImage> images = galleryService.findActiveImages();
        List<GalleryImage> foodImages = galleryService.findActiveByCategoryOrdered(GalleryImage.Category.FOOD);
        List<GalleryImage> ambianceImages = galleryService.findActiveByCategoryOrdered(GalleryImage.Category.AMBIANCE);
        List<GalleryImage> eventImages = galleryService.findActiveByCategoryOrdered(GalleryImage.Category.EVENTS);

        model.addAttribute("allImages", images);
        model.addAttribute("foodImages", foodImages);
        model.addAttribute("ambianceImages", ambianceImages);
        model.addAttribute("eventImages", eventImages);
        contactService.getContactInfo().ifPresent(info -> model.addAttribute("contactInfo", info));
        return "gallery";
    }
}
