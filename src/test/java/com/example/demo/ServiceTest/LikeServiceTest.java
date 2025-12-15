package com.example.demo.ServiceTest;

import com.example.demo.DTO.LikeDTO;
import com.example.demo.entities.Comment;
import com.example.demo.entities.Like;
import com.example.demo.entities.Recipe;
import com.example.demo.entities.User;
import com.example.demo.repositories.CommentRepository;
import com.example.demo.repositories.LikeRepository;
import com.example.demo.repositories.RecipeRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.LikeService;
import com.example.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private LikeService likeService;

    private Like like;
    private LikeDTO likeDTO;
    private User user;
    private Recipe recipe;
    private Comment comment;

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
        comment.setBodyText("Test Comment");

        like = new Like();
        like.setId(1L);
        like.setUser(user);
        like.setRecipe(recipe);

        likeDTO = new LikeDTO();
        likeDTO.setId(1L);
        likeDTO.setUserId(1L);
        likeDTO.setRecipeId(1L);
    }

    @Test
    void getAllLikes_ShouldReturnListOfLikeDTOs() {
        // Arrange
        Like like2 = new Like();
        like2.setId(2L);
        like2.setUser(user);
        like2.setRecipe(recipe);

        List<Like> likes = Arrays.asList(like, like2);
        when(likeRepository.findAll()).thenReturn(likes);

        // Act
        List<LikeDTO> result = likeService.getAllLikes();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(likeRepository, times(1)).findAll();
    }

    @Test
    void getLike_WithValidId_ShouldReturnLikeDTO() {
        // Arrange
        Long likeId = 1L;
        when(likeRepository.findById(likeId)).thenReturn(Optional.of(like));

        // Act
        LikeDTO result = likeService.getLike(likeId);

        // Assert
        assertNotNull(result);
        assertEquals(like.getId(), result.getId());
        verify(likeRepository, times(1)).findById(likeId);
    }

    @Test
    void getLike_WithInvalidId_ShouldThrowException() {
        // Arrange
        Long invalidId = 999L;
        when(likeRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act & Assert
        LikeNotFoundException exception = assertThrows(
                LikeNotFoundException.class,
                () -> likeService.getLike(invalidId)
        );

        assertTrue(exception.getMessage().contains("Like not found"));
        verify(likeRepository, times(1)).findById(invalidId);
    }

    @Test
    void createLike_ForRecipe_WithValidData_ShouldReturnCreatedLike() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(likeRepository.existsByUserUserIdAndRecipeId(1L, 1L)).thenReturn(false);
        when(likeRepository.save(any(Like.class))).thenReturn(like);

        // Act
        LikeDTO result = likeService.createLike(likeDTO);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(recipeRepository, times(1)).findById(1L);
        verify(likeRepository, times(1)).existsByUserUserIdAndRecipeId(1L, 1L);
        verify(likeRepository, times(1)).save(any(Like.class));
    }

    @Test
    void createLike_ForComment_WithValidData_ShouldReturnCreatedLike() {
        // Arrange
        likeDTO.setRecipeId(null);
        likeDTO.setCommentId(1L);

        like.setRecipe(null);
        like.setComment(comment);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(likeRepository.existsByUserUserIdAndCommentId(1L, 1L)).thenReturn(false);
        when(likeRepository.save(any(Like.class))).thenReturn(like);

        // Act
        LikeDTO result = likeService.createLike(likeDTO);

        // Assert
        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(commentRepository, times(1)).findById(1L);
        verify(likeRepository, times(1)).existsByUserUserIdAndCommentId(1L, 1L);
        verify(likeRepository, times(1)).save(any(Like.class));
    }

    @Test
    void createLike_WithNullUserId_ShouldThrowException() {
        // Arrange
        likeDTO.setUserId(null);

        // Act & Assert
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> likeService.createLike(likeDTO)
        );

        assertTrue(exception.getMessage().contains("User ID is required"));
        verify(userRepository, never()).findById(any());
        verify(likeRepository, never()).save(any(Like.class));
    }

    @Test
    void createLike_WithInvalidUserId_ShouldThrowException() {
        // Arrange
        likeDTO.setUserId(999L);
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class,
                () -> likeService.createLike(likeDTO)
        );

        assertTrue(exception.getMessage().contains("User not found"));
        verify(userRepository, times(1)).findById(999L);
        verify(likeRepository, never()).save(any(Like.class));
    }

    @Test
    void createLike_WithBothRecipeAndComment_ShouldThrowException() {
        // Arrange
        likeDTO.setRecipeId(1L);
        likeDTO.setCommentId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act & Assert
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> likeService.createLike(likeDTO)
        );

        assertTrue(exception.getMessage().contains("Like cannot be for both recipe and comment"));
        verify(likeRepository, never()).save(any(Like.class));
    }

    @Test
    void createLike_WithNeitherRecipeNorComment_ShouldThrowException() {
        // Arrange
        likeDTO.setRecipeId(null);
        likeDTO.setCommentId(null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act & Assert
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> likeService.createLike(likeDTO)
        );

        assertTrue(exception.getMessage().contains("Either recipe ID or comment ID is required"));
        verify(likeRepository, never()).save(any(Like.class));
    }

    @Test
    void createLike_WhenRecipeAlreadyLiked_ShouldThrowException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(likeRepository.existsByUserUserIdAndRecipeId(1L, 1L)).thenReturn(true);

        // Act & Assert
        LikeAlreadyExistsException exception = assertThrows(
                LikeAlreadyExistsException.class,
                () -> likeService.createLike(likeDTO)
        );

        assertTrue(exception.getMessage().contains("User already liked this recipe"));
        verify(likeRepository, times(1)).existsByUserUserIdAndRecipeId(1L, 1L);
        verify(likeRepository, never()).save(any(Like.class));
    }

    @Test
    void createLike_WhenCommentAlreadyLiked_ShouldThrowException() {
        // Arrange
        likeDTO.setRecipeId(null);
        likeDTO.setCommentId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(likeRepository.existsByUserUserIdAndCommentId(1L, 1L)).thenReturn(true);

        // Act & Assert
        LikeAlreadyExistsException exception = assertThrows(
                LikeAlreadyExistsException.class,
                () -> likeService.createLike(likeDTO)
        );

        assertTrue(exception.getMessage().contains("User already liked this comment"));
        verify(likeRepository, times(1)).existsByUserUserIdAndCommentId(1L, 1L);
        verify(likeRepository, never()).save(any(Like.class));
    }

    @Test
    void createLike_WithInvalidRecipeId_ShouldThrowException() {
        // Arrange
        likeDTO.setRecipeId(999L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(likeRepository.existsByUserUserIdAndRecipeId(1L, 999L)).thenReturn(false);
        when(recipeRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RecipeNotFoundException exception = assertThrows(
                RecipeNotFoundException.class,
                () -> likeService.createLike(likeDTO)
        );

        assertTrue(exception.getMessage().contains("Recipe not found"));
        verify(recipeRepository, times(1)).findById(999L);
        verify(likeRepository, never()).save(any(Like.class));
    }

    @Test
    void createLike_WithInvalidCommentId_ShouldThrowException() {
        // Arrange
        likeDTO.setRecipeId(null);
        likeDTO.setCommentId(999L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(likeRepository.existsByUserUserIdAndCommentId(1L, 999L)).thenReturn(false);
        when(commentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        CommentNotFoundException exception = assertThrows(
                CommentNotFoundException.class,
                () -> likeService.createLike(likeDTO)
        );

        assertTrue(exception.getMessage().contains("Comment not found"));
        verify(commentRepository, times(1)).findById(999L);
        verify(likeRepository, never()).save(any(Like.class));
    }

    @Test
    void deleteLike_WithValidId_ShouldDeleteLike() {
        // Arrange
        Long likeId = 1L;
        doNothing().when(likeRepository).deleteById(likeId);

        // Act
        likeService.deleteLike(likeId);

        // Assert
        verify(likeRepository, times(1)).deleteById(likeId);
    }

    @Test
    void deleteLikeByRecipeAndUser_WithValidIds_ShouldDeleteLike() {
        // Arrange
        Long recipeId = 1L;
        Long userId = 1L;
        when(likeRepository.findByRecipeIdAndUserUserId(recipeId, userId)).thenReturn(Optional.of(like));
        doNothing().when(likeRepository).delete(like);

        // Act
        likeService.deleteLikeByRecipeAndUser(recipeId, userId);

        // Assert
        verify(likeRepository, times(1)).findByRecipeIdAndUserUserId(recipeId, userId);
        verify(likeRepository, times(1)).delete(like);
    }

    @Test
    void deleteLikeByRecipeAndUser_WithInvalidIds_ShouldThrowException() {
        // Arrange
        Long recipeId = 999L;
        Long userId = 999L;
        when(likeRepository.findByRecipeIdAndUserUserId(recipeId, userId)).thenReturn(Optional.empty());

        // Act & Assert
        LikeNotFoundException exception = assertThrows(
                LikeNotFoundException.class,
                () -> likeService.deleteLikeByRecipeAndUser(recipeId, userId)
        );

        assertTrue(exception.getMessage().contains("Like not found"));
        verify(likeRepository, times(1)).findByRecipeIdAndUserUserId(recipeId, userId);
        verify(likeRepository, never()).delete(any(Like.class));
    }

    @Test
    void deleteLikeByCommentAndUser_WithValidIds_ShouldDeleteLike() {
        // Arrange
        Long commentId = 1L;
        Long userId = 1L;
        when(likeRepository.findByCommentIdAndUserUserId(commentId, userId)).thenReturn(Optional.of(like));
        doNothing().when(likeRepository).delete(like);

        // Act
        likeService.deleteLikeByCommentAndUser(commentId, userId);

        // Assert
        verify(likeRepository, times(1)).findByCommentIdAndUserUserId(commentId, userId);
        verify(likeRepository, times(1)).delete(like);
    }

    @Test
    void deleteLikeByCommentAndUser_WithInvalidIds_ShouldThrowException() {
        // Arrange
        Long commentId = 999L;
        Long userId = 999L;
        when(likeRepository.findByCommentIdAndUserUserId(commentId, userId)).thenReturn(Optional.empty());

        // Act & Assert
        LikeNotFoundException exception = assertThrows(
                LikeNotFoundException.class,
                () -> likeService.deleteLikeByCommentAndUser(commentId, userId)
        );

        assertTrue(exception.getMessage().contains("Like not found"));
        verify(likeRepository, times(1)).findByCommentIdAndUserUserId(commentId, userId);
        verify(likeRepository, never()).delete(any(Like.class));
    }
}