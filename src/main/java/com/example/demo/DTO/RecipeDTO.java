package com.example.demo.DTO;


import com.example.demo.entities.RecipeImage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecipeDTO {
    private Long id;
    private String name;
    private String description;
    private List<IngredientDTO> ingredients = new ArrayList<>();
    private Long authorId;
    private String authorName;
    private Date createdAt;
    private int commentCount;
    private int likeCount;
    boolean likedByCurrentUser;
    private List<RecipeImageDTO> images = new ArrayList<>();
    private List<String> stepByStepGuide = new ArrayList<>();
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isLikedByCurrentUser() {
        return likedByCurrentUser;
    }

    public void setLikedByCurrentUser(boolean likedByCurrentUser) {
        this.likedByCurrentUser = likedByCurrentUser;
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

    public List<String> getStepByStepGuide() {
        return stepByStepGuide;
    }

    public void setStepByStepGuide(List<String> stepByStepGuide) {
        this.stepByStepGuide = stepByStepGuide;
    }
}
