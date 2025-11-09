package com.example.demo.repositories;

import com.example.demo.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserUserIdAndRecipeId(Long userId, Long recipeId);
    boolean existsByUserUserIdAndCommentId(Long userId, Long commentId);
}
