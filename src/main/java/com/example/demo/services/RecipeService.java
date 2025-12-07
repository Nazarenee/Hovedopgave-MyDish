package com.example.demo.services;

import com.example.demo.DTO.RecipeDTO;
import com.example.demo.entities.Ingredient;
import com.example.demo.entities.Recipe;
import com.example.demo.entities.RecipeImage;
import com.example.demo.entities.User;
import com.example.demo.mappers.RecipeMapper;
import com.example.demo.repositories.RecipeRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.SecurityConfig;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeService(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    public List<RecipeDTO> getAllRecipes() {
        Long currentUserId = SecurityConfig.getCurrentUserId();
        List<Recipe> recipes = recipeRepository.findAll();
        return recipes.stream()
                .map(recipe -> RecipeMapper.toDTO(recipe, currentUserId))
                .collect(Collectors.toList());
    }

    public List<RecipeDTO> getUserRecipes(Long userId) {
        List<Recipe> recipes = recipeRepository.findByAuthorUserId(userId);
        return recipes.stream().map(recipe -> RecipeMapper.toDTO(recipe,userId)).collect(Collectors.toList());
    }

    public List<RecipeDTO> searchRecipe(String query){
        Long currentUserId = SecurityConfig.getCurrentUserId();
        List<Recipe> recipes = recipeRepository.findByNameContainingIgnoreCase(query);
        return recipes.stream()
                .map(recipe -> RecipeMapper.toDTO(recipe, currentUserId))
                .collect(Collectors.toList());
    }

    public RecipeDTO getRecipe(Long id) {
        Long currentUserId = SecurityConfig.getCurrentUserId();
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Recipe not found with id: " + id
                ));
        return RecipeMapper.toDTO(recipe, currentUserId);
    }




    public RecipeDTO createRecipe(RecipeDTO recipeDTO) {
        Recipe recipe = RecipeMapper.fromDTO(recipeDTO);
        if (recipeDTO.getAuthorId() != null) {
            User author = userRepository.findById(recipeDTO.getAuthorId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "User not found with id: " + recipeDTO.getAuthorId()
                    ));
            recipe.setAuthor(author);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Author ID is required"
            );
        }

        Recipe saved = recipeRepository.save(recipe);
        Long currentUserId = SecurityConfig.getCurrentUserId();
        return RecipeMapper.toDTO(saved, currentUserId);
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    public RecipeDTO saveRecipeToMyCollection(Long recipeId) {
        System.out.println("=== SAVE RECIPE SERVICE DEBUG ===");
        System.out.println("Recipe ID: " + recipeId);

        Long currentUserId = SecurityConfig.getCurrentUserId();
        System.out.println("Current User ID: " + currentUserId);

        if (currentUserId == null) {
            System.out.println("ERROR: Current user ID is null!");
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "User must be logged in to save recipes"
            );
        }

        System.out.println("Finding original recipe...");
        Recipe originalRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Recipe not found with id: " + recipeId
                ));
        System.out.println("Original recipe found: " + originalRecipe.getName());

        System.out.println("Finding current user...");

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found with id: " + currentUserId
                ));
        System.out.println("Current user found: " + currentUser.getUserName());

        if (originalRecipe.getAuthor().getUserId().equals(currentUserId)) {
            System.out.println("ERROR: User trying to save their own recipe!");

        if (originalRecipe.getAuthor().getUserId().equals(currentUserId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You cannot save your own recipe"
            );
        }

        System.out.println("Creating new recipe copy...");
        Recipe newRecipe = new Recipe();
        newRecipe.setName(originalRecipe.getName());
        newRecipe.setDescription(originalRecipe.getDescription());
        newRecipe.setEnableComments(originalRecipe.isEnableComments());
        newRecipe.setAuthor(currentUser);
        newRecipe.setCreatedAt(new Date());

        System.out.println("Copying " + originalRecipe.getIngredients().size() + " ingredients...");
        List<Ingredient> copiedIngredients = new ArrayList<>();
        for (Ingredient ing : originalRecipe.getIngredients()) {
            Ingredient newIng = new Ingredient();
            newIng.setName(ing.getName());
            newIng.setAmount(ing.getAmount());
            newIng.setUnit(ing.getUnit());
            newIng.setAverageCookingTime(ing.getAverageCookingTime());
            newIng.setRecipe(newRecipe);
            copiedIngredients.add(newIng);
        }
        newRecipe.setIngredients(copiedIngredients);

        System.out.println("Copying " + originalRecipe.getImages().size() + " images...");
        List<RecipeImage> copiedImages = new ArrayList<>();
        for (RecipeImage img : originalRecipe.getImages()) {
            RecipeImage newImg = new RecipeImage();
            newImg.setImageUrl(img.getImageUrl());
            newImg.setRecipe(newRecipe);
            copiedImages.add(newImg);
        }
        newRecipe.setImages(copiedImages);

        if (originalRecipe.getStepByStepGuide() != null) {
            System.out.println("Copying step-by-step guide...");
            newRecipe.setStepByStepGuide(new ArrayList<>(originalRecipe.getStepByStepGuide()));
        }

        System.out.println("Saving new recipe to database...");
        Recipe savedRecipe = recipeRepository.save(newRecipe);
        System.out.println("Recipe saved successfully with ID: " + savedRecipe.getId());
            newRecipe.setStepByStepGuide(new ArrayList<>(originalRecipe.getStepByStepGuide()));
        }

        Recipe savedRecipe = recipeRepository.save(newRecipe);

        return RecipeMapper.toDTO(savedRecipe, currentUserId);
    }
}