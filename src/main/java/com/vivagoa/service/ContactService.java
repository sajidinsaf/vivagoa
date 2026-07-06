package com.vivagoa.service;

import com.vivagoa.entity.ContactInfo;
import com.vivagoa.entity.ContactMessage;
import com.vivagoa.repository.ContactInfoRepository;
import com.vivagoa.repository.ContactMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContactService {

    private static final Logger logger = LoggerFactory.getLogger(ContactService.class);

    private final ContactInfoRepository contactInfoRepository;
    private final ContactMessageRepository contactMessageRepository;
    private final JavaMailSender mailSender;

    public ContactService(ContactInfoRepository contactInfoRepository,
                          ContactMessageRepository contactMessageRepository,
                          JavaMailSender mailSender) {
        this.contactInfoRepository = contactInfoRepository;
        this.contactMessageRepository = contactMessageRepository;
        this.mailSender = mailSender;
    }

    // Contact Info methods

    public Optional<ContactInfo> getContactInfo() {
        return contactInfoRepository.findFirstBy();
    }

    public ContactInfo saveContactInfo(ContactInfo contactInfo) {
        return contactInfoRepository.save(contactInfo);
    }

    // Contact Message methods

    public ContactMessage saveMessage(ContactMessage message) {
        ContactMessage saved = contactMessageRepository.save(message);
        sendEmailNotification(saved);
        return saved;
    }

    public List<ContactMessage> getAllMessages() {
        return contactMessageRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<ContactMessage> getUnreadMessages() {
        return contactMessageRepository.findByReadFalseOrderByCreatedAtDesc();
    }

    public Optional<ContactMessage> findMessageById(Long id) {
        return contactMessageRepository.findById(id);
    }

    public void markAsRead(Long id) {
        contactMessageRepository.findById(id).ifPresent(msg -> {
            msg.setRead(true);
            contactMessageRepository.save(msg);
        });
    }

    private void sendEmailNotification(ContactMessage message) {
        try {
            Optional<ContactInfo> contactInfo = getContactInfo();
            if (contactInfo.isPresent() && contactInfo.get().getEmail() != null) {
                SimpleMailMessage mail = new SimpleMailMessage();
                mail.setTo(contactInfo.get().getEmail());
                mail.setSubject("New Contact Message from Viva Goa Website: " + message.getSubject());
                mail.setText(String.format(
                        "New contact message received:\n\n" +
                        "Name: %s\nEmail: %s\nPhone: %s\nSubject: %s\n\nMessage:\n%s",
                        message.getName(),
                        message.getEmail(),
                        message.getPhone(),
                        message.getSubject(),
                        message.getMessage()
                ));
                mailSender.send(mail);
                logger.info("Email notification sent for contact message from: {}", message.getName());
            }
        } catch (Exception e) {
            logger.error("Failed to send email notification for contact message: {}", e.getMessage());
        }
    }
}
