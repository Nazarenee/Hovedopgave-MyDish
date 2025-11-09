package com.example.demo.mappers;

import com.example.demo.DTO.LikeDTO;
import com.example.demo.entities.Like;

public class LikeMapper {

    public static LikeDTO toDTO(Like like) {
        if (like == null) {
            return null;
        }

        LikeDTO dto = new LikeDTO();
        dto.setId(like.getId());

        if (like.getRecipe() != null) {
            dto.setRecipeId(like.getRecipe().getId());
        }

        if (like.getComment() != null) {
            dto.setCommentId(like.getComment().getId());
        }

        if (like.getUser() != null) {
            dto.setUserId(like.getUser().getUserId());
        }

        return dto;
    }

    public static Like fromDTO(LikeDTO dto) {
        if (dto == null) {
            return null;
        }
        Like like = new Like();
        like.setId(dto.getId());

        return like;
    }
}