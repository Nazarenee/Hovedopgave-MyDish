package com.example.demo.repositories;

import com.example.demo.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query("SELECT r FROM Recipe r JOIN FETCH r.author WHERE r.author.userId = :authorId")
    List<Recipe> findByAuthorUserId(@Param("authorId") Long authorId);

    @Query("SELECT r FROM Recipe r JOIN FETCH r.author WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Recipe> findByNameContainingIgnoreCase(@Param("keyword") String keyword);

    @Query("SELECT r FROM Recipe r JOIN FETCH r.author")
    @Override
    List<Recipe> findAll();

    @Query("SELECT r FROM Recipe r JOIN FETCH r.author WHERE r.id = :id")
    @Override
    Optional<Recipe> findById(@Param("id") Long id);
}