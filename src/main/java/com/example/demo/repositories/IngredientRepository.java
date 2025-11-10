package com.example.demo.repositories;

import com.example.demo.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByNameContainingIgnoreCase(String keyword);
}
