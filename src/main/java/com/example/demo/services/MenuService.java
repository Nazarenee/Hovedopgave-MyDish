package com.example.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.DTO.MenuDTO;
import com.example.demo.entities.Menu;
import com.example.demo.entities.User;
import com.example.demo.mappers.MenuMapper;
import com.example.demo.repositories.MenuRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    public MenuService(MenuRepository menuRepository, UserRepository userRepository) {
        this.menuRepository = menuRepository;
        this.userRepository = userRepository;
    }

    public List<MenuDTO> getAllMenus() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream().map(MenuMapper::toDTO).collect(Collectors.toList());
    }

    public MenuDTO getMenu(Long id) {
        Menu foundMenu =  menuRepository.findById(id).orElseThrow(() -> new RuntimeException("Menu not found"));
        return MenuMapper.toDTO(foundMenu);
    }

    public MenuDTO createMenu(MenuDTO menuDTO) {
        Menu menu = MenuMapper.fromDTO(menuDTO);
        User author = userRepository.findById(menuDTO.getAuthorId()).orElseThrow(() -> new RuntimeException("User not found"));
        menu.setAuthor(author);
        menuRepository.save(menu);
        return MenuMapper.toDTO(menu);
    }

    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }
}