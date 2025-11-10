package com.example.demo.controllers;

import com.example.demo.DTO.IngredientDTO;
import com.example.demo.services.IngredientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {
    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService){
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public List<IngredientDTO> getAllIngredients(){
        return ingredientService.getAllIngredients();
    }

    @GetMapping("/search")
    public List<IngredientDTO> searchIngredient(@RequestParam String query){
        return ingredientService.searchIngredient(query);
    }

    @GetMapping("/{id}")
    public IngredientDTO getIngredient(@PathVariable Long id){
        return ingredientService.getIngredient(id);
    }

    @PostMapping
    public IngredientDTO createIngredient(@RequestBody IngredientDTO ingredientDTO){
        return ingredientService.createIngredient(ingredientDTO);
    }

    @DeleteMapping
    public void deleteIngredient(@PathVariable Long id){
        ingredientService.deleteIngredient(id);
    }
}
