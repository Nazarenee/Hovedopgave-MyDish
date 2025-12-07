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
    private final CurrentUserService currentUserService;

    public RecipeService(RecipeRepository recipeRepository,
                         UserRepository userRepository,
                         CurrentUserService currentUserService) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    public List<RecipeDTO> getAllRecipes() {
        Long currentUserId = currentUserService.getCurrentUserId();
        List<Recipe> recipes = recipeRepository.findAll();
        return recipes.stream()
                .map(recipe -> RecipeMapper.toDTO(recipe, currentUserId))
                .collect(Collectors.toList());
    }

    public List<RecipeDTO> searchRecipe(String query){
        Long currentUserId = currentUserService.getCurrentUserId();
        List<Recipe> recipes = recipeRepository.findByNameContainingIgnoreCase(query);
        return recipes.stream()
                .map(recipe -> RecipeMapper.toDTO(recipe, currentUserId))
                .collect(Collectors.toList());
    }

    public RecipeDTO getRecipe(Long id) {
        Long currentUserId = currentUserService.getCurrentUserId();
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
        Long currentUserId = currentUserService.getCurrentUserId();
        return RecipeMapper.toDTO(saved, currentUserId);
    }

    public RecipeDTO saveRecipeToMyCollection(Long recipeId) {
        Long currentUserId = currentUserService.getCurrentUserId();

        if (currentUserId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "User must be logged in to save recipes"
            );
        }

        Recipe originalRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Recipe not found with id: " + recipeId
                ));

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found with id: " + currentUserId
                ));

        if (originalRecipe.getAuthor().getUserId().equals(currentUserId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You cannot save your own recipe"
            );
        }

        Recipe newRecipe = new Recipe();
        newRecipe.setName(originalRecipe.getName());
        newRecipe.setDescription(originalRecipe.getDescription());
        newRecipe.setEnableComments(originalRecipe.isEnableComments());
        newRecipe.setAuthor(currentUser);
        newRecipe.setCreatedAt(new Date());

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

        List<RecipeImage> copiedImages = new ArrayList<>();
        for (RecipeImage img : originalRecipe.getImages()) {
            RecipeImage newImg = new RecipeImage();
            newImg.setImageUrl(img.getImageUrl());
            newImg.setRecipe(newRecipe);
            copiedImages.add(newImg);
        }
        newRecipe.setImages(copiedImages);

        if (originalRecipe.getStepByStepGuide() != null) {
            newRecipe.setStepByStepGuide(new ArrayList<>(originalRecipe.getStepByStepGuide()));
        }

        Recipe savedRecipe = recipeRepository.save(newRecipe);
        return RecipeMapper.toDTO(savedRecipe, currentUserId);
    }

    public List<RecipeDTO> getUserRecipes(Long userId) {
        Long currentUserId = currentUserService.getCurrentUserId();
        List<Recipe> recipes = recipeRepository.findByAuthorUserId(userId);
        return recipes.stream()
                .map(recipe -> RecipeMapper.toDTO(recipe, currentUserId))
                .collect(Collectors.toList());
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }
}