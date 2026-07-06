package com.vivagoa.controller;

import com.vivagoa.entity.ContactMessage;
import com.vivagoa.service.ContactService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("contactMessage", new ContactMessage());
        contactService.getContactInfo().ifPresent(info -> model.addAttribute("contactInfo", info));
        return "contact";
    }

    @PostMapping("/contact")
    public String submitContact(@ModelAttribute ContactMessage contactMessage,
                                RedirectAttributes redirectAttributes) {
        contactService.saveMessage(contactMessage);
        redirectAttributes.addFlashAttribute("successMessage",
                "Thank you for your message! We will get back to you soon.");
        return "redirect:/contact";
    }
}
