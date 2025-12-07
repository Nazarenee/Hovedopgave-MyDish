package com.example.demo.ServiceTest;

import com.example.demo.DTO.CommentDTO;
import com.example.demo.entities.Comment;
import com.example.demo.entities.Recipe;
import com.example.demo.entities.User;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.repositories.RecipeRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.CommentService;
import com.example.demo.services.CurrentUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private CommentService commentService;

    private Comment comment;
    private CommentDTO commentDTO;
    private User user;
    private Recipe recipe;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setUserName("testUser");

        recipe = new Recipe();
        recipe.setId(1L);
        recipe.setName("Test Recipe");

        comment = new Comment();
        comment.setId(1L);
        comment.setBodyText("Test comment");
        comment.setUser(user);
        comment.setRecipe(recipe);
        comment.setCreated(LocalDateTime.now());

        when(currentUserService.getCurrentUserId()).thenReturn(1L);

        commentDTO = new CommentDTO();
        commentDTO.setId(1L);
        commentDTO.setBodyText("Test comment");
        commentDTO.setUserId(1L);
        commentDTO.setRecipeId(1L);
        commentDTO.setUserName("testUser");
        when(currentUserService.getCurrentUserId()).thenReturn(1L);
    }

    @Test
    void getAllComments_ShouldReturnListOfCommentDTOs() {
        // Arrange
        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setBodyText("Second comment");
        comment2.setUser(user);
        comment2.setRecipe(recipe);

        List<Comment> comments = Arrays.asList(comment, comment2);
        when(commentRepository.findAll()).thenReturn(comments);

        // Act
        List<CommentDTO> result = commentService.getAllComments();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(commentRepository, times(1)).findAll();
    }

    @Test
    void getComment_WithValidId_ShouldReturnCommentDTO() {
        // Arrange
        Long commentId = 1L;
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Act
        CommentDTO result = commentService.getComment(commentId);

        // Assert
        assertNotNull(result);
        assertEquals(comment.getId(), result.getId());
        assertEquals(comment.getBodyText(), result.getBodyText());
        verify(commentRepository, times(1)).findById(commentId);
    }

    @Test
    void getComment_WithInvalidId_ShouldThrowException() {
        // Arrange
        Long invalidId = 999L;
        when(commentRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> commentService.getComment(invalidId)
        );

        assertTrue(exception.getMessage().contains("Comment not found"));
        verify(commentRepository, times(1)).findById(invalidId);
    }

    @Test
    void createComment_WithValidData_ShouldReturnCreatedComment() {
        // Arrange
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Act
        CommentDTO result = commentService.createComment(commentDTO);

        // Assert
        assertNotNull(result);
        assertEquals(commentDTO.getBodyText(), result.getBodyText());
        verify(recipeRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void createComment_WithNullRecipeId_ShouldThrowException() {
        // Arrange
        commentDTO.setRecipeId(null);

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> commentService.createComment(commentDTO)
        );

        assertTrue(exception.getMessage().contains("Recipe ID is required"));
        verify(recipeRepository, never()).findById(any());
        verify(userRepository, never()).findById(any());
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void createComment_WithInvalidRecipeId_ShouldThrowException() {
        // Arrange
        commentDTO.setRecipeId(999L);
        when(recipeRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> commentService.createComment(commentDTO)
        );

        assertTrue(exception.getMessage().contains("Recipe not found"));
        verify(recipeRepository, times(1)).findById(999L);
        verify(userRepository, never()).findById(any());
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void createComment_WithNullUserId_ShouldThrowException() {
        // Arrange
        commentDTO.setUserId(null);
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> commentService.createComment(commentDTO)
        );

        assertTrue(exception.getMessage().contains("User ID is required"));
        verify(recipeRepository, times(1)).findById(1L);
        verify(userRepository, never()).findById(any());
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void createComment_WithInvalidUserId_ShouldThrowException() {
        // Arrange
        commentDTO.setUserId(999L);
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> commentService.createComment(commentDTO)
        );

        assertTrue(exception.getMessage().contains("User not found"));
        verify(recipeRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(999L);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void deleteComment_WithValidId_ShouldDeleteComment() {
        // Arrange
        Long commentId = 1L;
        when(commentRepository.existsById(commentId)).thenReturn(true);
        doNothing().when(commentRepository).deleteById(commentId);

        // Act
        commentService.deleteComment(commentId);

        // Assert
        verify(commentRepository, times(1)).existsById(commentId);
        verify(commentRepository, times(1)).deleteById(commentId);
    }

    @Test
    void deleteComment_WithInvalidId_ShouldThrowException() {
        // Arrange
        Long invalidId = 999L;
        when(commentRepository.existsById(invalidId)).thenReturn(false);

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> commentService.deleteComment(invalidId)
        );

        assertTrue(exception.getMessage().contains("Comment not found"));
        verify(commentRepository, times(1)).existsById(invalidId);
        verify(commentRepository, never()).deleteById(any());
    }
}