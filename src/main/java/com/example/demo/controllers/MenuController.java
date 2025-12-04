package com.example.demo.controllers;

import com.example.demo.DTO.MenuDTO;
import com.example.demo.services.MenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService){
        this.menuService = menuService;
    }

    @GetMapping
    public List<MenuDTO> getAllMenus(){
        return menuService.getAllMenus();
    }

    @GetMapping("/search")
    public List<MenuDTO> searchMenu(@RequestParam String query){
        return menuService.searchMenu(query);
    }

    @GetMapping("/{id}")
    public MenuDTO getMenu(@PathVariable Long id){
        return menuService.getMenu(id);
    }

    @GetMapping("/user/{userId}")
    public List<MenuDTO> getMenusByUser(@PathVariable Long userId){
        return menuService.getMenusByUser(userId);
    }

    @PostMapping
    public MenuDTO createMenu(@RequestBody MenuDTO menuDTO){
        return menuService.createMenu(menuDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteMenu(@PathVariable Long id){
        menuService.deleteMenu(id);
    }
}
