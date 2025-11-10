package com.example.demo.DTO;


public class RecipeImageDTO {
    private Long id;
    private String imageUrl;
    private Long recipeId;

    public RecipeImageDTO() {
    }

    public RecipeImageDTO(Long id, String imageUrl, Long recipeId) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.recipeId = recipeId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }
}