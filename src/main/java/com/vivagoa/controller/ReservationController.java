package com.vivagoa.controller;

import com.vivagoa.entity.Reservation;
import com.vivagoa.service.ContactService;
import com.vivagoa.service.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReservationController {

    private final ReservationService reservationService;
    private final ContactService contactService;

    public ReservationController(ReservationService reservationService, ContactService contactService) {
        this.reservationService = reservationService;
        this.contactService = contactService;
    }

    @GetMapping("/reservations")
    public String reservationForm(Model model) {
        model.addAttribute("reservation", new Reservation());
        contactService.getContactInfo().ifPresent(info -> model.addAttribute("contactInfo", info));
        return "reservation";
    }

    @PostMapping("/reservations")
    public String submitReservation(@ModelAttribute Reservation reservation,
                                    RedirectAttributes redirectAttributes) {
        reservation.setStatus(Reservation.Status.CONFIRMED);
        reservationService.save(reservation);
        redirectAttributes.addFlashAttribute("successMessage",
                "Your reservation has been confirmed! We look forward to welcoming you.");
        return "redirect:/reservations";
    }
}
