package com.example.demo.repositories;

import com.example.demo.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByRecipeIdAndUserUserId(Long recipeId, Long userId);
    boolean existsByUserUserIdAndRecipeId(Long userId, Long recipeId);
    boolean existsByUserUserIdAndCommentId(Long userId, Long commentId);
    Optional<Like> findByCommentIdAndUserUserId(Long commentId, Long userId);
}
