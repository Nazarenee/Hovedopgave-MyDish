package com.example.demo.repositories;

import com.example.demo.DTO.MenuDTO;
import com.example.demo.entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByNameContainingIgnoreCase(String query);
    List<Menu> findByAuthor_UserId(Long userId);
}
