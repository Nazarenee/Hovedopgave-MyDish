package com.example.demo.mappers;

import com.example.demo.DTO.MenuDTO;
import com.example.demo.entities.Menu;

import java.util.stream.Collectors;

public class MenuMapper {
    public static MenuDTO toDTO(Menu menu){
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setId(menu.getId());
        menuDTO.setName(menu.getName());
        menuDTO.setAuthorId(menu.getAuthor() != null ? menu.getAuthor().getUserId() : null);
        menuDTO.setRecipes(menu.getRecipes().stream().map(RecipeMapper::toDTO).collect(Collectors.toList()));
        return menuDTO;
    }

    public static Menu fromDTO(MenuDTO menuDTO){
        Menu menu = new Menu();
        menu.setName(menu.getName());
        // SET AUTHOR
        menu.setRecipes(menuDTO.getRecipes().stream().map(RecipeMapper::fromDTO).collect(Collectors.toList()));
        return menu;
    }
}
