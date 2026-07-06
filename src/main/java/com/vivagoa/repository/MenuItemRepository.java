package com.vivagoa.repository;

import com.vivagoa.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByCategoryOrderByDisplayOrder(String category);

    List<MenuItem> findByAvailableTrue();

    List<MenuItem> findByAvailableTrueOrderByCategoryAscDisplayOrderAsc();

    Optional<MenuItem> findByNameAndCategory(String name, String category);
}
