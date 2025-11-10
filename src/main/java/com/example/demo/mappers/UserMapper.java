package com.example.demo.mappers;

import com.example.demo.DTO.UserDTO;
import com.example.demo.entities.User;
import com.example.demo.entities.Comment;
import com.example.demo.entities.Recipe;
import com.example.demo.entities.Like;
import com.example.demo.entities.Menu;

import java.util.stream.Collectors;

public class UserMapper {
    public static UserDTO toDto(User user){
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        if (user.getRecipes() != null) {
            dto.setRecipeIds(user.getRecipes().stream()
                    .map(Recipe::getId)
                    .collect(Collectors.toList()));
        }

        if (user.getComments() != null) {
            dto.setCommentIds(user.getComments().stream()
                    .map(Comment::getId)
                    .collect(Collectors.toList()));
        }

        if (user.getLikes() != null) {
            dto.setLikeIds(user.getLikes().stream()
                    .map(Like::getId)
                    .collect(Collectors.toList()));
        }

        if (user.getMenus() != null) {
            dto.setMenuIds(user.getMenus().stream()
                    .map(Menu::getId)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}
