package com.example.demo.services;

import com.example.demo.entities.Ingredient;
import com.example.demo.repositories.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository){
        this.ingredientRepository=ingredientRepository;
    }

    public List<Ingredient> getAllIngredients(){
        return ingredientRepository.findAll();
    }

    public Ingredient getIngredient(Long id){
        return ingredientRepository.findById(id).orElseThrow(() -> new RuntimeException("Ingredient not found"));
    }

    public Ingredient createIngredient(Ingredient ingredient){
        return ingredientRepository.save(ingredient);
    }

    public void deleteIngredient(Long id){
        ingredientRepository.deleteById(id);
    }
}