package com.example.demo.DTO;

public class IngredientDTO {
    private Long id;
    private String name;
    private Integer averageCookingTime;
    private Long recipeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAverageCookingTime() {
        return averageCookingTime;
    }

    public void setAverageCookingTime(Integer averageCookingTime) {
        this.averageCookingTime = averageCookingTime;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }
}
