package com.example.demo.services;

import com.example.demo.DTO.IngredientDTO;
import com.example.demo.entities.Ingredient;
import com.example.demo.entities.Recipe;
import com.example.demo.mappers.IngredientMapper;
import com.example.demo.repositories.IngredientRepository;
import com.example.demo.repositories.RecipeRepository;
import com.example.exceptions.IngredientNotFoundException;
import com.example.exceptions.RecipeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;

    public IngredientService(IngredientRepository ingredientRepository, RecipeRepository recipeRepository) {
        this.ingredientRepository = ingredientRepository;
        this.recipeRepository = recipeRepository;
    }

    public List<IngredientDTO> getAllIngredients() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        return ingredients.stream().map(IngredientMapper::toDTO).collect(Collectors.toList());
    }

    public List<IngredientDTO> searchIngredient(String query){
        List<Ingredient> ingredients = ingredientRepository.findByNameContainingIgnoreCase(query);
        return ingredients.stream().map(IngredientMapper::toDTO).collect(Collectors.toList());
    }

    public IngredientDTO getIngredient(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new IngredientNotFoundException(id));
        return IngredientMapper.toDTO(ingredient);
    }

    public IngredientDTO createIngredient(IngredientDTO ingredientDTO) {
        Ingredient ingredient = IngredientMapper.fromDTO(ingredientDTO);
        Recipe foundRecipe = recipeRepository.findById(ingredientDTO.getRecipeId()).orElseThrow(() -> new RecipeNotFoundException(ingredientDTO.getRecipeId()));

        ingredient.setRecipe(foundRecipe);
        ingredientRepository.save(ingredient);
        return IngredientMapper.toDTO(ingredient);
    }

    public void deleteIngredient(Long id) {
        ingredientRepository.deleteById(id);
    }
}