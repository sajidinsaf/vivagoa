package com.vivagoa.service;

import com.vivagoa.entity.AdminUser;
import com.vivagoa.repository.AdminUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class AdminUserServiceTest {

    @Mock
    private AdminUserRepository adminUserRepository;

    @InjectMocks
    private AdminUserService adminUserService;

    private AdminUser adminUser;

    @BeforeEach
    void setUp() {
        adminUser = new AdminUser();
        adminUser.setId(1L);
        adminUser.setUsername("admin");
        adminUser.setPassword("$2a$10$encodedPassword");
        adminUser.setRole("ADMIN");
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetailsWithRoleAdmin() {
        when(adminUserRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));

        UserDetails userDetails = adminUserService.loadUserByUsername("admin");

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo("admin");
        assertThat(userDetails.getPassword()).isEqualTo("$2a$10$encodedPassword");
        assertThat(userDetails.getAuthorities())
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        verify(adminUserRepository).findByUsername("admin");
    }

    @Test
    void loadUserByUsername_shouldThrowUsernameNotFoundExceptionForUnknownUser() {
        when(adminUserRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminUserService.loadUserByUsername("unknown"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found: unknown");
    }

    @Test
    void save_shouldSaveAndReturnAdminUser() {
        when(adminUserRepository.save(any(AdminUser.class))).thenReturn(adminUser);

        AdminUser saved = adminUserService.save(adminUser);

        assertThat(saved).isNotNull();
        assertThat(saved.getUsername()).isEqualTo("admin");
        verify(adminUserRepository, times(1)).save(adminUser);
    }

    @Test
    void count_shouldReturnNumberOfAdminUsers() {
        when(adminUserRepository.count()).thenReturn(3L);

        long count = adminUserService.count();

        assertThat(count).isEqualTo(3L);
        verify(adminUserRepository).count();
    }

    @Test
    void count_shouldReturnZeroWhenNoUsers() {
        when(adminUserRepository.count()).thenReturn(0L);

        long count = adminUserService.count();

        assertThat(count).isEqualTo(0L);
    }
}
