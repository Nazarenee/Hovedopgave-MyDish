package com.example.demo.DTO;

import java.util.List;

public class UserDTO {
    private Long userId;
    private String userName;
    private List<Long> recipeIds;
    private List<Long> commentIds;
    private List<Long> likeIds;
    private List<Long> menuIds;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Long> getRecipeIds() {
        return recipeIds;
    }

    public void setRecipeIds(List<Long> recipeIds) {
        this.recipeIds = recipeIds;
    }

    public List<Long> getCommentIds() {
        return commentIds;
    }

    public void setCommentIds(List<Long> commentIds) {
        this.commentIds = commentIds;
    }

    public List<Long> getLikeIds() {
        return likeIds;
    }

    public void setLikeIds(List<Long> likeIds) {
        this.likeIds = likeIds;
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<Long> menuIds) {
        this.menuIds = menuIds;
    }
}