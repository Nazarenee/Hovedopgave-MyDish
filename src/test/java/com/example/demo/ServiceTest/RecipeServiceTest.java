package com.example.demo.ServiceTest;

import com.example.demo.DTO.RecipeDTO;
import com.example.demo.entities.Recipe;
import com.example.demo.entities.User;
import com.example.demo.repositories.RecipeRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.CurrentUserService;
import com.example.demo.services.RecipeService;
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
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private RecipeService recipeService;

    private Recipe recipe;
    private RecipeDTO recipeDTO;
    private User author;

    @BeforeEach
    void setUp() {
        author = new User();
        author.setUserName("testUser");

        recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Test Recipe");
        recipe.setDescription("Test Description");
        recipe.setAuthor(author);

        recipeDTO = new RecipeDTO();
        recipeDTO.setId(1L);
        recipeDTO.setName("Test Recipe");
        recipeDTO.setDescription("Test Description");
        recipeDTO.setAuthorId(1L);
        recipeDTO.setAuthorName("testUser");
        recipeDTO.setCommentCount(0);
        recipeDTO.setLikeCount(0);
        recipeDTO.setLikedByCurrentUser(false);
        recipeDTO.setEnableComments(true);
        when(currentUserService.getCurrentUserId()).thenReturn(1L);
    }

    @Test
        void getAllRecipes_ShouldReturnListOfRecipeDTOs() {
        // Arrange
        Recipe recipe2 = new Recipe();
        recipe2.setId(2L);
        recipe2.setName("Second Recipe");

        List<Recipe> recipes = Arrays.asList(recipe, recipe2);
        when(recipeRepository.findAll()).thenReturn(recipes);

        // Act
        List<RecipeDTO> result = recipeService.getAllRecipes();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(recipeRepository, times(1)).findAll();
    }

    @Test
    void searchRecipe_WithValidQuery_ShouldReturnMatchingRecipes() {
        // Arrange
        String query = "Test";
        List<Recipe> recipes = Arrays.asList(recipe);
        when(recipeRepository.findByNameContainingIgnoreCase(query)).thenReturn(recipes);

        // Act
        List<RecipeDTO> result = recipeService.searchRecipe(query);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getName().contains("Test"));
        verify(recipeRepository, times(1)).findByNameContainingIgnoreCase(query);
    }

    @Test
    void getRecipe_WithValidId_ShouldReturnRecipeDTO() {
        // Arrange
        Long recipeId = 1L;
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

        // Act
        RecipeDTO result = recipeService.getRecipe(recipeId);

        // Assert
        assertNotNull(result);
        assertEquals(recipe.getId(), result.getId());
        assertEquals(recipe.getName(), result.getName());
        verify(recipeRepository, times(1)).findById(recipeId);
    }

    @Test
    void getRecipe_WithInvalidId_ShouldThrowException() {
        // Arrange
        Long invalidId = 999L;
        when(recipeRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> recipeService.getRecipe(invalidId)
        );

        assertTrue(exception.getMessage().contains("Recipe not found"));
        verify(recipeRepository, times(1)).findById(invalidId);
    }

    @Test
    void createRecipe_WithValidDTO_ShouldReturnCreatedRecipe() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(author));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);

        // Act
        RecipeDTO result = recipeService.createRecipe(recipeDTO);

        // Assert
        assertNotNull(result);
        assertEquals(recipeDTO.getName(), result.getName());
        verify(userRepository, times(1)).findById(1L);
        verify(recipeRepository, times(1)).save(any(Recipe.class));
    }

    @Test
    void createRecipe_WithInvalidAuthorId_ShouldThrowException() {
        // Arrange
        recipeDTO.setAuthorId(999L);
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> recipeService.createRecipe(recipeDTO)
        );

        assertTrue(exception.getMessage().contains("User not found"));
        verify(userRepository, times(1)).findById(999L);
        verify(recipeRepository, never()).save(any(Recipe.class));
    }

    @Test
    void createRecipe_WithNullAuthorId_ShouldThrowException() {
        // Arrange
        recipeDTO.setAuthorId(null);

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> recipeService.createRecipe(recipeDTO)
        );

        assertTrue(exception.getMessage().contains("Author ID is required"));
        verify(userRepository, never()).findById(any());
        verify(recipeRepository, never()).save(any(Recipe.class));
    }

    @Test
    void deleteRecipe_WithValidId_ShouldCallRepositoryDelete() {
        // Arrange
        Long recipeId = 1L;
        doNothing().when(recipeRepository).deleteById(recipeId);

        // Act
        recipeService.deleteRecipe(recipeId);

        // Assert
        verify(recipeRepository, times(1)).deleteById(recipeId);
    }
}