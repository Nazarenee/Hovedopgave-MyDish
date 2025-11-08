package services;

import DTO.RecipeDTO;
import entities.Recipe;
import mappers.RecipeMapper;
import repositories.RecipeRepository;

import java.util.List;
import java.util.stream.Collectors;

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
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
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