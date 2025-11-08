package com.example.demo.services;

import com.example.demo.DTO.RecipeDTO;
import com.example.demo.entities.Recipe;
import com.example.demo.mappers.RecipeMapper;
import com.example.demo.repositories.RecipeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<RecipeDTO> getAllRecipes() {
        List<Recipe> recipes = recipeRepository.findAll();
        return recipes.stream()
                .map(RecipeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public RecipeDTO getRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Recipe not found with id: " + id
                ));
        return RecipeMapper.toDTO(recipe);
    }

    public RecipeDTO createRecipe(RecipeDTO recipeDTO) {
        Recipe recipe = RecipeMapper.fromDTO(recipeDTO);
        Recipe saved = recipeRepository.save(recipe);
        return RecipeMapper.toDTO(saved);
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }
}