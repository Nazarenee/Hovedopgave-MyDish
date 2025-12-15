package com.example.demo.services;

import com.example.demo.DTO.CommentDTO;
import com.example.demo.entities.Comment;
import com.example.demo.entities.Recipe;
import com.example.demo.entities.User;
import com.example.demo.mappers.CommentMapper;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.repositories.RecipeRepository;
import com.example.demo.repositories.UserRepository;
import com.example.exceptions.BadRequestException;
import com.example.exceptions.CommentNotFoundException;
import com.example.exceptions.RecipeNotFoundException;
import com.example.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public CommentService(CommentRepository commentRepository,
                          RecipeRepository recipeRepository,
                          UserRepository userRepository,
                          CurrentUserService currentUserService) {
        this.commentRepository = commentRepository;
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    public List<CommentDTO> getAllComments() {
        Long currentUserId = currentUserService.getCurrentUserId();
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                .map(comment -> CommentMapper.toDTO(comment, currentUserId))
                .collect(Collectors.toList());
    }

    public CommentDTO getComment(Long id) {
        Long currentUserId = currentUserService.getCurrentUserId();
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
        return CommentMapper.toDTO(comment, currentUserId);
    }

    public List<CommentDTO> getCommentsByRecipe(Long recipeId) {
        Long currentUserId = currentUserService.getCurrentUserId();
        List<Comment> comments = commentRepository.findByRecipeIdOrderByCreatedDesc(recipeId);
        return comments.stream()
                .map(comment -> CommentMapper.toDTO(comment, currentUserId))
                .collect(Collectors.toList());
    }

    public CommentDTO createComment(CommentDTO commentDTO) {
        Comment comment = CommentMapper.fromDTO(commentDTO);

        if (commentDTO.getRecipeId() != null) {
            Recipe recipe = recipeRepository.findById(commentDTO.getRecipeId())
                    .orElseThrow(() -> new RecipeNotFoundException(commentDTO.getRecipeId()));
            comment.setRecipe(recipe);
        } else {
            throw new BadRequestException("Recipe ID is required");
        }

        if (commentDTO.getUserId() != null) {
            User user = userRepository.findById(commentDTO.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(commentDTO.getUserId()));
            comment.setUser(user);
        } else {
            throw new BadRequestException("User ID is required");
        }

        Comment saved = commentRepository.save(comment);
        Long currentUserId = currentUserService.getCurrentUserId();
        return CommentMapper.toDTO(saved, currentUserId);
    }

    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new CommentNotFoundException(id);
        }
        commentRepository.deleteById(id);
    }
}