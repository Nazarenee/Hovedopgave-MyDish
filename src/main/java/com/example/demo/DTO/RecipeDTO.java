package com.example.demo.DTO;


import com.example.demo.entities.RecipeImage;

import java.util.Date;
import java.util.List;

public class RecipeDTO {
    private Long id;
    private String name;
    private String description;
    private List<IngredientDTO> ingredients;
    private Long authorId;
    private Date createdAt;
    private List<CommentDTO> comments;
    private List<LikeDTO> likes;
    private List<RecipeImageDTO> images;
    private boolean enableComments;

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

    public List<IngredientDTO> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientDTO> ingredients) {
        this.ingredients = ingredients;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public List<LikeDTO> getLikes() {
        return likes;
    }

    public void setLikes(List<LikeDTO> likes) {
        this.likes = likes;
    }

    public List<RecipeImageDTO> getImages() {
        return images;
    }

    public void setImages(List<RecipeImageDTO> images) {
        this.images = images;
    }

    public boolean isEnableComments() {
        return enableComments;
    }

    public void setEnableComments(boolean enableComments) {
        this.enableComments = enableComments;
    }
}
