package com.example.demo.mappers;

import com.example.demo.DTO.IngredientDTO;
import com.example.demo.entities.Ingredient;

public class IngredientMapper {
    public static IngredientDTO toDTO(Ingredient ingredient){
        IngredientDTO dto = new IngredientDTO();
        dto.setId(ingredient.getId());
        dto.setName(ingredient.getName());
        dto.setAmount(ingredient.getAmount());
        dto.setUnit(ingredient.getUnit());
        dto.setRecipeId(ingredient.getRecipe() != null ? ingredient.getRecipe().getId() : null);
        dto.setAverageCookingTime(ingredient.getAverageCookingTime());
        return dto;
    }

    public static Ingredient fromDTO(IngredientDTO ingredientDTO){
        Ingredient ingredient = new Ingredient();
        ingredient.setId(ingredientDTO.getId());
        ingredient.setName(ingredientDTO.getName());
        ingredient.setAmount(ingredientDTO.getAmount());
        ingredient.setUnit(ingredientDTO.getUnit());
        ingredient.setAverageCookingTime(ingredient.getAverageCookingTime());
        return ingredient;
    }
}
