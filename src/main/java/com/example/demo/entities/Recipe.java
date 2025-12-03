package com.example.demo.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Ingredient> ingredients = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeImage> images = new ArrayList<>();
    @ElementCollection
    @CollectionTable(name = "recipe_steps", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "step")
    @OrderColumn(name = "step_order")
    private List<String> stepByStepGuide = new ArrayList<>();

    private boolean enableComments;

    public Recipe(Long id, String name, String description, List<Ingredient> ingredients, User author, Date createdAt, List<Comment> comments, List<Like> likes, List<RecipeImage> images,List<String> stepByStepGuide, boolean enableComments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.author = author;
        this.createdAt = createdAt;
        this.comments = comments;
        this.likes = likes;
        this.images = images;
        this.enableComments = enableComments;
        this.stepByStepGuide = stepByStepGuide;
    }

    public Recipe() {
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<RecipeImage> getImages() {
        return images;
    }

    public void setImages(List<RecipeImage> images) {
        this.images = images;
    }

    public boolean isEnableComments() {
        return enableComments;
    }

    public void setEnableComments(boolean enableComments) {
        this.enableComments = enableComments;
    }

    public List<String> getStepByStepGuide() {
        return stepByStepGuide;
    }

    public void setStepByStepGuide(List<String> stepByStepGuide) {
        this.stepByStepGuide = stepByStepGuide;
    }
}