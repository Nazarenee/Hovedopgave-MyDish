package com.example.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.DTO.MenuDTO;
import com.example.demo.entities.Menu;
import com.example.demo.entities.Recipe;
import com.example.demo.entities.User;
import com.example.demo.mappers.MenuMapper;
import com.example.demo.repositories.MenuRepository;
import com.example.demo.repositories.RecipeRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    public MenuService(MenuRepository menuRepository, UserRepository userRepository, RecipeRepository recipeRepository) {
        this.menuRepository = menuRepository;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    public List<MenuDTO> getAllMenus() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream().map(MenuMapper::toDTO).collect(Collectors.toList());
    }

    public MenuDTO getMenu(Long id) {
        Menu foundMenu =  menuRepository.findById(id).orElseThrow(() -> new RuntimeException("Menu not found"));
        return MenuMapper.toDTO(foundMenu);
    }

    public List<MenuDTO> getMenusByUser(Long userId) {
        List<Menu> menus = menuRepository.findByAuthor_UserId(userId);
        return menus.stream().map(MenuMapper::toDTO).collect(Collectors.toList());
    }

    public MenuDTO createMenu(MenuDTO menuDTO) {
        Menu menu = MenuMapper.fromDTO(menuDTO);
        User author = userRepository.findById(menuDTO.getAuthorId()).orElseThrow(() -> new RuntimeException("User not found"));
        menu.setAuthor(author);

        if (menuDTO.getRecipeIds() != null && !menuDTO.getRecipeIds().isEmpty()) {
            List<Recipe> recipes = recipeRepository.findAllById(menuDTO.getRecipeIds());
            menu.setRecipes(recipes);
        }

        menuRepository.save(menu);
        return MenuMapper.toDTO(menu);
    }

    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }

    public List<MenuDTO> searchMenu(String query) {
        List<Menu> menus =  menuRepository.findByNameContainingIgnoreCase(query);
       return menus.stream().map(MenuMapper::toDTO).collect(Collectors.toList());
    }
}