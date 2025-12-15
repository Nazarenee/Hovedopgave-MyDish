package com.example.demo.ServiceTest;

import com.example.demo.DTO.IngredientDTO;
import com.example.demo.entities.Ingredient;
import com.example.demo.entities.Recipe;
import com.example.demo.entities.Unit;
import com.example.demo.repositories.IngredientRepository;
import com.example.demo.repositories.RecipeRepository;
import com.example.demo.services.IngredientService;
import com.example.exceptions.IngredientNotFoundException;
import com.example.exceptions.RecipeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private IngredientService ingredientService;

    private Ingredient ingredient;
    private IngredientDTO ingredientDTO;
    private Recipe recipe;

    @BeforeEach
    void setUp() {
        recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Test Recipe");

        ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Flour");
        ingredient.setAmount(250.0);
        ingredient.setUnit(Unit.GRAM);
        ingredient.setRecipe(recipe);
        ingredient.setAverageCookingTime(30);

        ingredientDTO = new IngredientDTO();
        ingredientDTO.setId(1L);
        ingredientDTO.setName("Flour");
        ingredientDTO.setAmount(250.0);
        ingredientDTO.setUnit(Unit.GRAM);
        ingredientDTO.setRecipeId(1L);
        ingredientDTO.setAverageCookingTime(30);
    }

    @Test
    void getAllIngredients_ShouldReturnListOfIngredientDTOs() {
        // Arrange
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);
        ingredient2.setName("Sugar");
        ingredient2.setAmount(100.0);
        ingredient2.setUnit(Unit.GRAM);
        ingredient2.setRecipe(recipe);

        List<Ingredient> ingredients = Arrays.asList(ingredient, ingredient2);
        when(ingredientRepository.findAll()).thenReturn(ingredients);

        // Act
        List<IngredientDTO> result = ingredientService.getAllIngredients();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ingredientRepository, times(1)).findAll();
    }

    @Test
    void searchIngredient_ShouldReturnFilteredIngredients() {
        // Arrange
        String query = "Flour";
        List<Ingredient> ingredients = Arrays.asList(ingredient);
        when(ingredientRepository.findByNameContainingIgnoreCase(query)).thenReturn(ingredients);

        // Act
        List<IngredientDTO> result = ingredientService.searchIngredient(query);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Flour", result.get(0).getName());
        verify(ingredientRepository, times(1)).findByNameContainingIgnoreCase(query);
    }

    @Test
    void getIngredient_WithValidId_ShouldReturnIngredientDTO() {
        // Arrange
        Long ingredientId = 1L;
        when(ingredientRepository.findById(ingredientId)).thenReturn(Optional.of(ingredient));

        // Act
        IngredientDTO result = ingredientService.getIngredient(ingredientId);

        // Assert
        assertNotNull(result);
        assertEquals(ingredient.getId(), result.getId());
        assertEquals(ingredient.getName(), result.getName());
        assertEquals(ingredient.getAmount(), result.getAmount());
        assertEquals(ingredient.getUnit(), result.getUnit());
        verify(ingredientRepository, times(1)).findById(ingredientId);
    }

    @Test
    void getIngredient_WithInvalidId_ShouldThrowException() {
        // Arrange
        Long invalidId = 999L;
        when(ingredientRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        IngredientNotFoundException exception = assertThrows(
                IngredientNotFoundException.class,
                () -> ingredientService.getIngredient(invalidId)
        );

        assertTrue(exception.getMessage().contains("Ingredient not found"));
        verify(ingredientRepository, times(1)).findById(invalidId);
    }

    @Test
    void createIngredient_WithValidData_ShouldReturnCreatedIngredient() {
        // Arrange
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(ingredient);

        // Act
        IngredientDTO result = ingredientService.createIngredient(ingredientDTO);

        // Assert
        assertNotNull(result);
        assertEquals(ingredientDTO.getName(), result.getName());
        assertEquals(ingredientDTO.getAmount(), result.getAmount());
        assertEquals(ingredientDTO.getUnit(), result.getUnit());
        verify(recipeRepository, times(1)).findById(1L);
        verify(ingredientRepository, times(1)).save(any(Ingredient.class));
    }

    @Test
    void createIngredient_WithInvalidRecipeId_ShouldThrowException() {
        // Arrange
        ingredientDTO.setRecipeId(999L);
        when(recipeRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RecipeNotFoundException exception = assertThrows(
                RecipeNotFoundException.class,
                () -> ingredientService.createIngredient(ingredientDTO)
        );

        assertTrue(exception.getMessage().contains("Recipe not found"));
        verify(recipeRepository, times(1)).findById(999L);
        verify(ingredientRepository, never()).save(any(Ingredient.class));
    }

    @Test
    void deleteIngredient_WithValidId_ShouldDeleteIngredient() {
        // Arrange
        Long ingredientId = 1L;
        doNothing().when(ingredientRepository).deleteById(ingredientId);

        // Act
        ingredientService.deleteIngredient(ingredientId);

        // Assert
        verify(ingredientRepository, times(1)).deleteById(ingredientId);
    }
}