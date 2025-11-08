package com.example.demo.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "ingredients")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
    @Column(nullable = true)
    private Integer averageCookingTime;

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

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Integer getAverageCookingTime() {
        return averageCookingTime;
    }

    public void setAverageCookingTime(Integer averageCookingTime) {
        this.averageCookingTime = averageCookingTime;
    }
}
