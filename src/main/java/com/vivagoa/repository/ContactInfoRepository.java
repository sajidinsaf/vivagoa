package com.vivagoa.repository;

import com.vivagoa.entity.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactInfoRepository extends JpaRepository<ContactInfo, Long> {

    Optional<ContactInfo> findFirstBy();
}
