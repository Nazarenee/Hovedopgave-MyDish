package com.example.demo.mappers;

import com.example.demo.DTO.IngredientDTO;
import com.example.demo.DTO.RecipeDTO;
import com.example.demo.DTO.RecipeImageDTO;
import com.example.demo.entities.Ingredient;
import com.example.demo.entities.Recipe;
import com.example.demo.entities.RecipeImage;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

public class RecipeMapper {
    public static RecipeDTO toDTO(Recipe recipe) {
        RecipeDTO dto = new RecipeDTO();
        dto.setId(recipe.getId());
        dto.setName(recipe.getName());
        dto.setDescription(recipe.getDescription());
        dto.setAuthorId(recipe.getAuthor() != null ? recipe.getAuthor().getUserId() : null);
        dto.setAuthorName(recipe.getAuthor() != null ? recipe.getAuthor().getUserName() : null);
        dto.setCreatedAt(recipe.getCreatedAt());
        dto.setEnableComments(recipe.isEnableComments());

        dto.setCommentCount(recipe.getComments() != null ? recipe.getComments().size() : 0);
        dto.setLikeCount(recipe.getLikes() != null ? recipe.getLikes().size() : 0);

        dto.setLikedByCurrentUser(false);

        if (recipe.getImages() != null) {
            dto.setImages(recipe.getImages().stream()
                    .map(img -> new RecipeImageDTO(img.getId(), img.getImageUrl(), recipe.getId()))
                    .collect(Collectors.toList()));
        }

        if (recipe.getIngredients() != null) {
            dto.setIngredients(recipe.getIngredients().stream()
                    .map(ing -> {
                        IngredientDTO ingDTO = new IngredientDTO();
                        ingDTO.setId(ing.getId());
                        ingDTO.setName(ing.getName());
                        ingDTO.setAmount(ing.getAmount());
                        ingDTO.setUnit(ing.getUnit());
                        ingDTO.setAverageCookingTime(ing.getAverageCookingTime());
                        ingDTO.setRecipeId(recipe.getId());
                        return ingDTO;
                    })
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public static RecipeDTO toDTO(Recipe recipe, Long currentUserId) {
        RecipeDTO dto = toDTO(recipe);

        if (currentUserId != null && recipe.getLikes() != null) {
            boolean liked = recipe.getLikes().stream()
                    .anyMatch(like -> like.getUser() != null &&
                            like.getUser().getUserId().equals(currentUserId));
            dto.setLikedByCurrentUser(liked);
        }

        return dto;
    }

    public static Recipe fromDTO(RecipeDTO dto) {
        Recipe recipe = new Recipe();
        recipe.setName(dto.getName());
        recipe.setDescription(dto.getDescription());
        recipe.setEnableComments(dto.isEnableComments());

        recipe.setCreatedAt(new Date());
        if (dto.getImages() != null) {
            recipe.setImages(dto.getImages().stream()
                    .map(imgDTO -> {
                        RecipeImage img = new RecipeImage();
                        img.setImageUrl(imgDTO.getImageUrl());
                        img.setRecipe(recipe);
                        return img;
                    })
                    .collect(Collectors.toList()));
        } else {
            recipe.setImages(new ArrayList<>());
        }
        if (dto.getIngredients() != null) {
            recipe.setIngredients(dto.getIngredients().stream()
                    .map(ingDTO -> {
                        Ingredient ing = new Ingredient();
                        ing.setName(ingDTO.getName());
                        ing.setAmount(ingDTO.getAmount());
                        ing.setUnit(ingDTO.getUnit());
                        ing.setAverageCookingTime(ingDTO.getAverageCookingTime());
                        ing.setRecipe(recipe);
                        return ing;
                    })
                    .collect(Collectors.toList()));
        } else {
            recipe.setIngredients(new ArrayList<>());
        }

        return recipe;
    }
}
