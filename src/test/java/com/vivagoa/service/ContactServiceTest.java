package com.vivagoa.service;

import com.vivagoa.entity.ContactInfo;
import com.vivagoa.entity.ContactMessage;
import com.vivagoa.repository.ContactInfoRepository;
import com.vivagoa.repository.ContactMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ContactServiceTest {

    @Mock
    private ContactInfoRepository contactInfoRepository;

    @Mock
    private ContactMessageRepository contactMessageRepository;

    @Mock
    private JavaMailSender mailSender;

    private ContactService contactService;

    private ContactInfo contactInfo;
    private ContactMessage contactMessage;

    @BeforeEach
    void setUp() {
        contactService = new ContactService(contactInfoRepository, contactMessageRepository, mailSender);

        contactInfo = new ContactInfo();
        contactInfo.setId(1L);
        contactInfo.setPhone1("+91-832-2345678");
        contactInfo.setPhone2("+91-9876543210");
        contactInfo.setEmail("info@vivagoa.com");
        contactInfo.setAddress("123 Beach Road, Calangute, Goa");
        contactInfo.setOpeningHours("11:00 AM - 11:00 PM");

        contactMessage = new ContactMessage();
        contactMessage.setId(1L);
        contactMessage.setName("Test User");
        contactMessage.setEmail("test@example.com");
        contactMessage.setPhone("9876543210");
        contactMessage.setSubject("Inquiry");
        contactMessage.setMessage("I would like to know about your menu.");
        contactMessage.setCreatedAt(LocalDateTime.now());
        contactMessage.setRead(false);
    }

    @Test
    void getContactInfo_shouldReturnContactInfoWhenExists() {
        when(contactInfoRepository.findFirstBy()).thenReturn(Optional.of(contactInfo));

        Optional<ContactInfo> result = contactService.getContactInfo();

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("info@vivagoa.com");
        assertThat(result.get().getPhone1()).isEqualTo("+91-832-2345678");
        verify(contactInfoRepository).findFirstBy();
    }

    @Test
    void getContactInfo_shouldReturnEmptyWhenNotExists() {
        when(contactInfoRepository.findFirstBy()).thenReturn(Optional.empty());

        Optional<ContactInfo> result = contactService.getContactInfo();

        assertThat(result).isEmpty();
    }

    @Test
    void saveContactInfo_shouldSaveAndReturnContactInfo() {
        when(contactInfoRepository.save(any(ContactInfo.class))).thenReturn(contactInfo);

        ContactInfo saved = contactService.saveContactInfo(contactInfo);

        assertThat(saved).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("info@vivagoa.com");
        verify(contactInfoRepository, times(1)).save(contactInfo);
    }

    @Test
    void saveMessage_shouldSaveMessageAndSendEmail() {
        when(contactMessageRepository.save(any(ContactMessage.class))).thenReturn(contactMessage);
        when(contactInfoRepository.findFirstBy()).thenReturn(Optional.of(contactInfo));
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        ContactMessage saved = contactService.saveMessage(contactMessage);

        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test User");
        verify(contactMessageRepository, times(1)).save(contactMessage);
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void saveMessage_shouldSaveEvenWhenEmailFails() {
        when(contactMessageRepository.save(any(ContactMessage.class))).thenReturn(contactMessage);
        when(contactInfoRepository.findFirstBy()).thenReturn(Optional.of(contactInfo));
        doThrow(new RuntimeException("Mail server down")).when(mailSender).send(any(SimpleMailMessage.class));

        ContactMessage saved = contactService.saveMessage(contactMessage);

        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test User");
        verify(contactMessageRepository).save(contactMessage);
    }

    @Test
    void saveMessage_shouldNotSendEmailWhenNoContactInfo() {
        when(contactMessageRepository.save(any(ContactMessage.class))).thenReturn(contactMessage);
        when(contactInfoRepository.findFirstBy()).thenReturn(Optional.empty());

        ContactMessage saved = contactService.saveMessage(contactMessage);

        assertThat(saved).isNotNull();
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void getAllMessages_shouldReturnMessagesOrderedByDateDesc() {
        ContactMessage message2 = new ContactMessage();
        message2.setId(2L);
        message2.setName("Another User");
        message2.setCreatedAt(LocalDateTime.now().minusDays(1));

        when(contactMessageRepository.findAllByOrderByCreatedAtDesc())
                .thenReturn(Arrays.asList(contactMessage, message2));

        List<ContactMessage> result = contactService.getAllMessages();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Test User");
        verify(contactMessageRepository).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void getAllMessages_shouldReturnEmptyListWhenNoMessages() {
        when(contactMessageRepository.findAllByOrderByCreatedAtDesc())
                .thenReturn(Collections.emptyList());

        List<ContactMessage> result = contactService.getAllMessages();

        assertThat(result).isEmpty();
    }

    @Test
    void getUnreadMessages_shouldReturnOnlyUnreadMessages() {
        when(contactMessageRepository.findByReadFalseOrderByCreatedAtDesc())
                .thenReturn(Collections.singletonList(contactMessage));

        List<ContactMessage> result = contactService.getUnreadMessages();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).isRead()).isFalse();
        verify(contactMessageRepository).findByReadFalseOrderByCreatedAtDesc();
    }

    @Test
    void markAsRead_shouldMarkMessageAsRead() {
        when(contactMessageRepository.findById(1L)).thenReturn(Optional.of(contactMessage));
        when(contactMessageRepository.save(any(ContactMessage.class))).thenReturn(contactMessage);

        contactService.markAsRead(1L);

        assertThat(contactMessage.isRead()).isTrue();
        verify(contactMessageRepository).findById(1L);
        verify(contactMessageRepository).save(contactMessage);
    }

    @Test
    void markAsRead_shouldDoNothingWhenMessageNotFound() {
        when(contactMessageRepository.findById(99L)).thenReturn(Optional.empty());

        contactService.markAsRead(99L);

        verify(contactMessageRepository).findById(99L);
        verify(contactMessageRepository, never()).save(any());
    }
}
