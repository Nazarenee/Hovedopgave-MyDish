package com.example.demo.services;

import java.util.List;

import com.example.demo.entities.Menu;
import com.example.demo.repositories.MenuRepository;
import org.springframework.stereotype.Service;

@Service
public class MenuService {
    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    public Menu getMenu(Long id) {
        return menuRepository.findById(id).orElseThrow(() -> new RuntimeException("Menu not found"));
    }

    public Menu createMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }
}