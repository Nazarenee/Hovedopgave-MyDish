package com.example.demo.repositories;

import com.example.demo.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByAuthorUserId(Long authorId);
}