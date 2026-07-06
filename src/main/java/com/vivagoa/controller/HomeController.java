package com.vivagoa.controller;

import com.vivagoa.entity.GalleryImage;
import com.vivagoa.service.ContactService;
import com.vivagoa.service.GalleryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final GalleryService galleryService;
    private final ContactService contactService;

    public HomeController(GalleryService galleryService, ContactService contactService) {
        this.galleryService = galleryService;
        this.contactService = contactService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<GalleryImage> featuredImages = galleryService.findActiveImages();
        model.addAttribute("galleryImages", featuredImages);
        contactService.getContactInfo().ifPresent(info -> model.addAttribute("contactInfo", info));
        return "home";
    }
}
