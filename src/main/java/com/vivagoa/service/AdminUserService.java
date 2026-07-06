package com.vivagoa.service;

import com.vivagoa.entity.AdminUser;
import com.vivagoa.repository.AdminUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdminUserService implements UserDetailsService {

    private final AdminUserRepository adminUserRepository;

    public AdminUserService(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser adminUser = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new User(
                adminUser.getUsername(),
                adminUser.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + adminUser.getRole()))
        );
    }

    public Optional<AdminUser> findByUsername(String username) {
        return adminUserRepository.findByUsername(username);
    }

    public AdminUser save(AdminUser adminUser) {
        return adminUserRepository.save(adminUser);
    }

    public List<AdminUser> findAll() {
        return adminUserRepository.findAll();
    }

    public long count() {
        return adminUserRepository.count();
    }
}
