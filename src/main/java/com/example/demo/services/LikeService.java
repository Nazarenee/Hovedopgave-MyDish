package com.example.demo.services;

import com.example.demo.DTO.LikeDTO;
import com.example.demo.entities.Comment;
import com.example.demo.entities.Like;
import com.example.demo.entities.Recipe;
import com.example.demo.entities.User;
import com.example.demo.mappers.LikeMapper;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.repositories.LikeRepository;
import com.example.demo.repositories.RecipeRepository;
import com.example.demo.repositories.UserRepository;
import com.example.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final CommentRepository commentRepository;

    public LikeService(LikeRepository likeRepository,UserRepository userRepository,  RecipeRepository recipeRepository, CommentRepository commentRepository){
        this.likeRepository=likeRepository;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
        this.commentRepository = commentRepository;
    }

    public List<LikeDTO> getAllLikes(){
        List<Like> likes = likeRepository.findAll();
        return likes.stream().map(LikeMapper::toDTO).collect(Collectors.toList());
    }

    public LikeDTO getLike(Long id){
        Like like =  likeRepository.findById(id).orElseThrow(() -> new LikeNotFoundException("Like not found with id: " + id));
        return LikeMapper.toDTO(like);
    }

    public void deleteLikeByRecipeAndUser(Long recipeId, Long userId) {
        Like like = likeRepository.findByRecipeIdAndUserUserId(recipeId, userId)
                .orElseThrow(() -> new LikeNotFoundException("Like not found for recipe ID: " + recipeId + " and user ID: " + userId));
        likeRepository.delete(like);
    }

    public LikeDTO createLike(LikeDTO likeDTO) {
        Like like = LikeMapper.fromDTO(likeDTO);

        if (likeDTO.getUserId() != null) {
            User user = userRepository.findById(likeDTO.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(likeDTO.getUserId()));
            like.setUser(user);
        } else {
            throw new BadRequestException("User ID is required");
        }

        boolean hasRecipe = likeDTO.getRecipeId() != null;
        boolean hasComment = likeDTO.getCommentId() != null;

        if (hasRecipe && hasComment) {
            throw new BadRequestException("Like cannot be for both recipe and comment");
        }

        if (!hasRecipe && !hasComment) {
            throw new BadRequestException("Either recipe ID or comment ID is required");
        }

        if (hasRecipe) {
            boolean alreadyLiked = likeRepository.existsByUserUserIdAndRecipeId(
                    likeDTO.getUserId(),
                    likeDTO.getRecipeId()
            );
            if (alreadyLiked) {
                throw new LikeAlreadyExistsException("User already liked this recipe");
            }

            Recipe recipe = recipeRepository.findById(likeDTO.getRecipeId())
                    .orElseThrow(() -> new RecipeNotFoundException(likeDTO.getRecipeId()));
            like.setRecipe(recipe);
        }

        if (hasComment) {
            boolean alreadyLiked = likeRepository.existsByUserUserIdAndCommentId(
                    likeDTO.getUserId(),
                    likeDTO.getCommentId()
            );
            if (alreadyLiked) {
                throw new LikeAlreadyExistsException("User already liked this comment");
            }
            Comment comment = commentRepository.findById(likeDTO.getCommentId())
                    .orElseThrow(() -> new CommentNotFoundException(likeDTO.getCommentId()));
            like.setComment(comment);
        }

        Like saved = likeRepository.save(like);
        return LikeMapper.toDTO(saved);
    }

    public void deleteLikeByCommentAndUser(Long commentId, Long userId) {
        Like like = likeRepository.findByCommentIdAndUserUserId(commentId, userId)
                .orElseThrow(() -> new LikeNotFoundException("Like not found for comment ID: " + commentId + " and user ID: " + userId));
        likeRepository.delete(like);
    }

    public void deleteLike(Long id){
        likeRepository.deleteById(id);
    }
}