package com.example.demo.DTO;

import java.time.LocalDateTime;
import java.util.List;

public class CommentDTO {
    private Long id;
    private String bodyText;
    private Long userId;
    private Long recipeId;
    private LocalDateTime created;
    private List<LikeDTO> likes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public List<LikeDTO> getLikes() {
        return likes;
    }

    public void setLikes(List<LikeDTO> likes) {
        this.likes = likes;
    }
}
