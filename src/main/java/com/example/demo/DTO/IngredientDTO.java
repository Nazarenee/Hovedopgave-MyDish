package com.example.demo.DTO;

import com.example.demo.entities.Unit;

public class IngredientDTO {
    private Long id;
    private String name;
    private Double amount;
    private Unit unit;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
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
