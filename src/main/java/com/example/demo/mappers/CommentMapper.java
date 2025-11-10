package com.example.demo.mappers;

import com.example.demo.DTO.CommentDTO;
import com.example.demo.entities.Comment;

import java.util.stream.Collectors;

public class CommentMapper {
    public static CommentDTO toDTO(Comment comment){
        if (comment == null) {
            return null;
        }
        CommentDTO dto  = new CommentDTO();
        dto.setId(comment.getId());
        dto.setCreated(comment.getCreated());
        dto.setBodyText(comment.getBodyText());
        dto.setUserName(comment.getUser().getUserName());
        if (comment.getRecipe() != null) {
            dto.setRecipeId(comment.getRecipe().getId());
        }
        if (comment.getUser() != null) {
            dto.setUserId(comment.getUser().getUserId());
        }
        if (comment.getLikes() != null) {
            dto.setLikeCount(comment.getLikes().size());
        }
        return dto;
    }

    public static Comment fromDTO(CommentDTO dto) {
        if (dto == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setId(dto.getId());
        comment.setBodyText(dto.getBodyText());
        comment.setCreated(dto.getCreated());

        return comment;
    }
}
