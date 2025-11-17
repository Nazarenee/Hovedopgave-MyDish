package com.example.demo.ControllerTest;

import com.example.demo.DTO.RecipeDTO;
import com.example.demo.controllers.RecipeController;
import com.example.demo.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private RecipeController recipeController;

    private RecipeDTO recipeDTO;

    @BeforeEach
    void setUp() {
        recipeDTO = new RecipeDTO();
        recipeDTO.setId(1L);
        recipeDTO.setName("Test Recipe");
        recipeDTO.setDescription("Test Description");
        recipeDTO.setAuthorId(1L);
    }

    @Test
    void getAllRecipes_ShouldReturnListOfRecipes() {
        // Arrange
        RecipeDTO recipe2 = new RecipeDTO();
        recipe2.setId(2L);
        recipe2.setName("Second Recipe");
        recipe2.setDescription("Second Description");

        List<RecipeDTO> expectedRecipes = Arrays.asList(recipeDTO, recipe2);
        when(recipeService.getAllRecipes()).thenReturn(expectedRecipes);

        // Act
        List<RecipeDTO> result = recipeController.getAllRecipes();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedRecipes, result);
        verify(recipeService, times(1)).getAllRecipes();
    }

    @Test
    void searchRecipe_ShouldReturnFilteredRecipes() {
        // Arrange
        String query = "Test";
        List<RecipeDTO> expectedRecipes = Arrays.asList(recipeDTO);
        when(recipeService.searchRecipe(query)).thenReturn(expectedRecipes);

        // Act
        List<RecipeDTO> result = recipeController.searchRecipe(query);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(recipeDTO.getName(), result.get(0).getName());
        verify(recipeService, times(1)).searchRecipe(query);
    }

    @Test
    void getRecipe_WithValidId_ShouldReturnRecipe() {
        // Arrange
        Long recipeId = 1L;
        when(recipeService.getRecipe(recipeId)).thenReturn(recipeDTO);

        // Act
        RecipeDTO result = recipeController.getRecipe(recipeId);

        // Assert
        assertNotNull(result);
        assertEquals(recipeDTO.getId(), result.getId());
        assertEquals(recipeDTO.getName(), result.getName());
        assertEquals(recipeDTO.getDescription(), result.getDescription());
        verify(recipeService, times(1)).getRecipe(recipeId);
    }

    @Test
    void createRecipe_WithValidDTO_ShouldReturnCreatedRecipe() {
        // Arrange
        when(recipeService.createRecipe(any(RecipeDTO.class))).thenReturn(recipeDTO);

        // Act
        RecipeDTO result = recipeController.createRecipe(recipeDTO);

        // Assert
        assertNotNull(result);
        assertEquals(recipeDTO.getId(), result.getId());
        assertEquals(recipeDTO.getName(), result.getName());
        assertEquals(recipeDTO.getDescription(), result.getDescription());
        verify(recipeService, times(1)).createRecipe(any(RecipeDTO.class));
    }

    @Test
    void deleteRecipe_WithValidId_ShouldCallServiceDelete() {
        // Arrange
        Long recipeId = 1L;
        doNothing().when(recipeService).deleteRecipe(recipeId);

        // Act
        recipeController.deleteRecipe(recipeId);

        // Assert
        verify(recipeService, times(1)).deleteRecipe(recipeId);
    }
}