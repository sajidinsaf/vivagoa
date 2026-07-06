package com.vivagoa.controller;

import com.vivagoa.entity.*;
import com.vivagoa.service.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ReservationService reservationService;
    private final GalleryService galleryService;
    private final MenuService menuService;
    private final ContactService contactService;

    public AdminController(ReservationService reservationService,
                           GalleryService galleryService,
                           MenuService menuService,
                           ContactService contactService) {
        this.reservationService = reservationService;
        this.galleryService = galleryService;
        this.menuService = menuService;
        this.contactService = contactService;
    }

    // Dashboard
    @GetMapping
    public String dashboard(Model model) {
        List<Reservation> todayReservations = reservationService.findByDate(LocalDate.now());
        model.addAttribute("todayReservations", todayReservations);
        model.addAttribute("totalToday", todayReservations.size());
        long unreadMessages = contactService.getUnreadMessages().size();
        model.addAttribute("unreadMessages", unreadMessages);
        return "admin/dashboard";
    }

    // Login page
    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    // Reservations
    @GetMapping("/reservations")
    public String listReservations(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String status,
            Model model) {

        List<Reservation> reservations;

        if (startDate != null && endDate != null) {
            reservations = reservationService.findByDateRange(startDate, endDate);
        } else if (status != null && !status.isEmpty()) {
            reservations = reservationService.findByStatus(Reservation.Status.valueOf(status));
        } else {
            reservations = reservationService.findAll();
        }

        model.addAttribute("reservations", reservations);
        model.addAttribute("statuses", Reservation.Status.values());
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("selectedStatus", status);
        return "admin/reservations";
    }

    @PostMapping("/reservations/{id}/status")
    public String updateReservationStatus(@PathVariable Long id,
                                          @RequestParam String status,
                                          RedirectAttributes redirectAttributes) {
        reservationService.updateStatus(id, Reservation.Status.valueOf(status));
        redirectAttributes.addFlashAttribute("successMessage", "Reservation status updated.");
        return "redirect:/admin/reservations";
    }

    @PostMapping("/reservations/{id}/billing")
    public String updateReservationBilling(@PathVariable Long id,
                                           @RequestParam BigDecimal amount,
                                           RedirectAttributes redirectAttributes) {
        reservationService.updateBilling(id, amount);
        redirectAttributes.addFlashAttribute("successMessage", "Billing amount updated.");
        return "redirect:/admin/reservations";
    }

    // Gallery
    @GetMapping("/gallery")
    public String manageGallery(Model model) {
        model.addAttribute("images", galleryService.findAll());
        model.addAttribute("categories", GalleryImage.Category.values());
        model.addAttribute("newImage", new GalleryImage());
        return "admin/gallery";
    }

    @PostMapping("/gallery")
    public String addGalleryImage(@ModelAttribute GalleryImage image,
                                  @RequestParam(value = "imageFile", required = false) MultipartFile file,
                                  RedirectAttributes redirectAttributes) {
        if (file != null && !file.isEmpty()) {
            image.setFileName(file.getOriginalFilename());
            // File upload handling would go here in a full implementation
        }
        galleryService.save(image);
        redirectAttributes.addFlashAttribute("successMessage", "Gallery image added.");
        return "redirect:/admin/gallery";
    }

    @PostMapping("/gallery/{id}/delete")
    public String deleteGalleryImage(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        galleryService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Gallery image deleted.");
        return "redirect:/admin/gallery";
    }

    @PostMapping("/gallery/{id}/update")
    public String updateGalleryImage(@PathVariable Long id,
                                     @RequestParam String title,
                                     @RequestParam String description,
                                     @RequestParam GalleryImage.Category category,
                                     @RequestParam int displayOrder,
                                     @RequestParam boolean active,
                                     RedirectAttributes redirectAttributes) {
        galleryService.findById(id).ifPresent(image -> {
            image.setTitle(title);
            image.setDescription(description);
            image.setCategory(category);
            image.setDisplayOrder(displayOrder);
            image.setActive(active);
            galleryService.save(image);
        });
        redirectAttributes.addFlashAttribute("successMessage", "Gallery image updated.");
        return "redirect:/admin/gallery";
    }

    // Contact Info
    @GetMapping("/contact")
    public String manageContact(Model model) {
        contactService.getContactInfo().ifPresentOrElse(
                info -> model.addAttribute("contactInfo", info),
                () -> model.addAttribute("contactInfo", new ContactInfo())
        );
        return "admin/contact";
    }

    @PostMapping("/contact")
    public String updateContactInfo(@ModelAttribute ContactInfo contactInfo,
                                    RedirectAttributes redirectAttributes) {
        contactService.saveContactInfo(contactInfo);
        redirectAttributes.addFlashAttribute("successMessage", "Contact information updated.");
        return "redirect:/admin/contact";
    }

    // Messages
    @GetMapping("/messages")
    public String viewMessages(Model model) {
        model.addAttribute("messages", contactService.getAllMessages());
        return "admin/messages";
    }

    @PostMapping("/messages/{id}/read")
    public String markMessageRead(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        contactService.markAsRead(id);
        return "redirect:/admin/messages";
    }
}
