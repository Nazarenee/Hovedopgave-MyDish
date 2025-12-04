package com.example.demo.mappers;

import com.example.demo.DTO.MenuDTO;
import com.example.demo.entities.Menu;
import com.example.demo.security.SecurityConfig;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MenuMapper {
    public static MenuDTO toDTO(Menu menu){
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setId(menu.getId());
        menuDTO.setName(menu.getName());
        menuDTO.setDescription(menu.getDescription());
        menuDTO.setAuthorId(menu.getAuthor() != null ? menu.getAuthor().getUserId() : null);
        menuDTO.setAuthorName(menu.getAuthor() != null ? menu.getAuthor().getUserName() : null);
        Long currentUserId = SecurityConfig.getCurrentUserId();
        if (menu.getRecipes() != null) {
            menuDTO.setRecipes(menu.getRecipes().stream()
                    .map(recipe -> RecipeMapper.toDTO(recipe, currentUserId))
                    .collect(Collectors.toList()));
        }        return menuDTO;
    }

    public static Menu fromDTO(MenuDTO menuDTO){
        Menu menu = new Menu();
        menu.setName(menuDTO.getName());
        menu.setDescription(menuDTO.getDescription());
        menu.setRecipes(menuDTO.getRecipes() != null ? menuDTO.getRecipes().stream().map(RecipeMapper::fromDTO).collect(Collectors.toList()) : new ArrayList<>());
        return menu;
    }
}
