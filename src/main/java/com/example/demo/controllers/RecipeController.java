package com.example.demo.controllers;

import com.example.demo.DTO.RecipeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.services.RecipeService;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService){
        this.recipeService = recipeService;
    }

    @GetMapping
    public List<RecipeDTO> getAllRecipes(){
        return recipeService.getAllRecipes();
    }

    @GetMapping("/{id}")
    public RecipeDTO getRecipe(@PathVariable Long id) {
        return recipeService.getRecipe(id);
    }

    @PostMapping
    public RecipeDTO createRecipe(@RequestBody RecipeDTO recipeDTO) {
        return recipeService.createRecipe(recipeDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
    }
}
