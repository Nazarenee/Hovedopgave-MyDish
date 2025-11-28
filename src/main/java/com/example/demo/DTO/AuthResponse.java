package com.example.demo.DTO;

public class AuthResponse {
    private String token;
    private String userName;
    private Long userId;

    public AuthResponse(String token, String userName, Long userId) {
        this.token = token;
        this.userName = userName;
        this.userId = userId;
    }

    public AuthResponse(String token, String userName) {
        this.token = token;
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}