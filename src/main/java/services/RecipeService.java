package services;

import entities.Recipe;
import repositories.RecipeRepository;
import java.util.List;
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe getRecipe(Long id) {
        return recipeRepository.findById(id).orElseThrow(() -> new RuntimeException("Recipe not found"));
    }

    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }
}