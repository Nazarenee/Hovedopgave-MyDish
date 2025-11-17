package com.example.demo;

import com.example.demo.DTO.IngredientDTO;
import com.example.demo.controllers.IngredientController;
import com.example.demo.entities.Unit;
import com.example.demo.services.IngredientService;
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
class IngredientControllerTest {

    @Mock
    private IngredientService ingredientService;

    @InjectMocks
    private IngredientController ingredientController;

    private IngredientDTO ingredientDTO;

    @BeforeEach
    void setUp() {
        ingredientDTO = new IngredientDTO();
        ingredientDTO.setId(1L);
        ingredientDTO.setName("Flour");
        ingredientDTO.setAmount(250.0);
        ingredientDTO.setUnit(Unit.GRAM);
        ingredientDTO.setRecipeId(1L);
        ingredientDTO.setAverageCookingTime(30);
    }

    @Test
    void getAllIngredients_ShouldReturnListOfIngredients() {
        // Arrange
        IngredientDTO ingredient2 = new IngredientDTO();
        ingredient2.setId(2L);
        ingredient2.setName("Sugar");
        ingredient2.setAmount(100.0);
        ingredient2.setUnit(Unit.GRAM);

        List<IngredientDTO> expectedIngredients = Arrays.asList(ingredientDTO, ingredient2);
        when(ingredientService.getAllIngredients()).thenReturn(expectedIngredients);

        // Act
        List<IngredientDTO> result = ingredientController.getAllIngredients();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedIngredients, result);
        verify(ingredientService, times(1)).getAllIngredients();
    }

    @Test
    void searchIngredient_ShouldReturnFilteredIngredients() {
        // Arrange
        String query = "Flour";
        List<IngredientDTO> expectedIngredients = Arrays.asList(ingredientDTO);
        when(ingredientService.searchIngredient(query)).thenReturn(expectedIngredients);

        // Act
        List<IngredientDTO> result = ingredientController.searchIngredient(query);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ingredientDTO.getName(), result.get(0).getName());
        verify(ingredientService, times(1)).searchIngredient(query);
    }

    @Test
    void getIngredient_WithValidId_ShouldReturnIngredient() {
        // Arrange
        Long ingredientId = 1L;
        when(ingredientService.getIngredient(ingredientId)).thenReturn(ingredientDTO);

        // Act
        IngredientDTO result = ingredientController.getIngredient(ingredientId);

        // Assert
        assertNotNull(result);
        assertEquals(ingredientDTO.getId(), result.getId());
        assertEquals(ingredientDTO.getName(), result.getName());
        assertEquals(ingredientDTO.getAmount(), result.getAmount());
        assertEquals(ingredientDTO.getUnit(), result.getUnit());
        verify(ingredientService, times(1)).getIngredient(ingredientId);
    }

    @Test
    void createIngredient_WithValidDTO_ShouldReturnCreatedIngredient() {
        // Arrange
        when(ingredientService.createIngredient(any(IngredientDTO.class))).thenReturn(ingredientDTO);

        // Act
        IngredientDTO result = ingredientController.createIngredient(ingredientDTO);

        // Assert
        assertNotNull(result);
        assertEquals(ingredientDTO.getId(), result.getId());
        assertEquals(ingredientDTO.getName(), result.getName());
        assertEquals(ingredientDTO.getAmount(), result.getAmount());
        assertEquals(ingredientDTO.getUnit(), result.getUnit());
        verify(ingredientService, times(1)).createIngredient(any(IngredientDTO.class));
    }

    @Test
    void deleteIngredient_WithValidId_ShouldCallServiceDelete() {
        // Arrange
        Long ingredientId = 1L;
        doNothing().when(ingredientService).deleteIngredient(ingredientId);

        // Act
        ingredientController.deleteIngredient(ingredientId);

        // Assert
        verify(ingredientService, times(1)).deleteIngredient(ingredientId);
    }
}