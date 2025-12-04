package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public class UserDTO {
    private Long userId;

    @JsonProperty("username")
    @NotBlank(message = "Username is required", groups = OnCreate.class)
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String userName;

    @NotBlank(message = "Password is required", groups = OnCreate.class)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "Password must be at least 8 characters and contain 1 uppercase, 1 lowercase, and 1 digit",
            groups = {OnCreate.class, OnPasswordChange.class}
    )
    private String password;

    private String email;
    private String profileImage;

    private String currentPassword;
    private String newPassword;

    private String token;


    private List<Long> recipeIds;
    private List<Long> commentIds;
    private List<Long> likeIds;
    private List<Long> menuIds;

    public interface OnCreate {}
    public interface OnPasswordChange {}

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}