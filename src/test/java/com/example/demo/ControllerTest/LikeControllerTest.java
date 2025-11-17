package com.example.demo.ControllerTest;

import com.example.demo.DTO.LikeDTO;
import com.example.demo.controllers.LikeController;
import com.example.demo.services.LikeService;
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
class LikeControllerTest {

    @Mock
    private LikeService likeService;

    @InjectMocks
    private LikeController likeController;

    private LikeDTO likeDTO;

    @BeforeEach
    void setUp() {
        likeDTO = new LikeDTO();
        likeDTO.setId(1L);
        likeDTO.setUserId(1L);
        likeDTO.setRecipeId(1L);
        likeDTO.setCommentId(null);
    }

    @Test
    void getAllLikes_ShouldReturnListOfLikes() {
        // Arrange
        LikeDTO like2 = new LikeDTO();
        like2.setId(2L);
        like2.setUserId(2L);
        like2.setRecipeId(1L);

        List<LikeDTO> expectedLikes = Arrays.asList(likeDTO, like2);
        when(likeService.getAllLikes()).thenReturn(expectedLikes);

        // Act
        List<LikeDTO> result = likeController.getAllLikes();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedLikes, result);
        verify(likeService, times(1)).getAllLikes();
    }

    @Test
    void getLike_WithValidId_ShouldReturnLike() {
        // Arrange
        Long likeId = 1L;
        when(likeService.getLike(likeId)).thenReturn(likeDTO);

        // Act
        LikeDTO result = likeController.getLike(likeId);

        // Assert
        assertNotNull(result);
        assertEquals(likeDTO.getId(), result.getId());
        assertEquals(likeDTO.getUserId(), result.getUserId());
        assertEquals(likeDTO.getRecipeId(), result.getRecipeId());
        verify(likeService, times(1)).getLike(likeId);
    }

    @Test
    void createLike_WithValidDTO_ShouldReturnCreatedLike() {
        // Arrange
        when(likeService.createLike(any(LikeDTO.class))).thenReturn(likeDTO);

        // Act
        LikeDTO result = likeController.createLike(likeDTO);

        // Assert
        assertNotNull(result);
        assertEquals(likeDTO.getId(), result.getId());
        assertEquals(likeDTO.getUserId(), result.getUserId());
        assertEquals(likeDTO.getRecipeId(), result.getRecipeId());
        verify(likeService, times(1)).createLike(any(LikeDTO.class));
    }

    @Test
    void deleteLike_WithValidId_ShouldCallServiceDelete() {
        // Arrange
        Long likeId = 1L;
        doNothing().when(likeService).deleteLike(likeId);

        // Act
        likeController.deleteLike(likeId);

        // Assert
        verify(likeService, times(1)).deleteLike(likeId);
    }
}