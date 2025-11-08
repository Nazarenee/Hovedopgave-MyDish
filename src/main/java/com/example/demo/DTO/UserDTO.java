package com.example.demo.DTO;

import java.util.List;

public class UserDTO {
    private Long userId;
    private String userName;
    private List<RecipeDTO> recipes;
    private List<CommentDTO> comments;
    private List<LikeDTO> likes;
    private List<MenuDTO> menus;
}
