package com.example.demo;

import com.example.demo.DTO.CommentDTO;
import com.example.demo.controllers.CommentController;
import com.example.demo.services.CommentService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private CommentDTO commentDTO;

    @BeforeEach
    void setUp() {
        commentDTO = new CommentDTO();
        commentDTO.setId(1L);
        commentDTO.setBodyText("Test comment");
        commentDTO.setUserId(1L);
        commentDTO.setRecipeId(1L);
        commentDTO.setUserName("testUser");
        commentDTO.setLikeCount(0);
        commentDTO.setLikedByCurrentUser(false);
    }

    @Test
    void getAllComments_ShouldReturnListOfComments() {
        // Arrange
        CommentDTO comment2 = new CommentDTO();
        comment2.setId(2L);
        comment2.setBodyText("Second comment");
        comment2.setUserId(2L);
        comment2.setRecipeId(1L);

        List<CommentDTO> expectedComments = Arrays.asList(commentDTO, comment2);
        when(commentService.getAllComments()).thenReturn(expectedComments);

        // Act
        List<CommentDTO> result = commentController.getAllComments();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedComments, result);
        verify(commentService, times(1)).getAllComments();
    }

    @Test
    void getComment_WithValidId_ShouldReturnComment() {
        // Arrange
        Long commentId = 1L;
        when(commentService.getComment(commentId)).thenReturn(commentDTO);

        // Act
        CommentDTO result = commentController.getComment(commentId);

        // Assert
        assertNotNull(result);
        assertEquals(commentDTO.getId(), result.getId());
        assertEquals(commentDTO.getBodyText(), result.getBodyText());
        assertEquals(commentDTO.getUserId(), result.getUserId());
        assertEquals(commentDTO.getRecipeId(), result.getRecipeId());
        verify(commentService, times(1)).getComment(commentId);
    }

    @Test
    void createComment_WithValidDTO_ShouldReturnCreatedComment() {
        // Arrange
        when(commentService.createComment(any(CommentDTO.class))).thenReturn(commentDTO);

        // Act
        CommentDTO result = commentController.createComment(commentDTO);

        // Assert
        assertNotNull(result);
        assertEquals(commentDTO.getId(), result.getId());
        assertEquals(commentDTO.getBodyText(), result.getBodyText());
        assertEquals(commentDTO.getUserId(), result.getUserId());
        verify(commentService, times(1)).createComment(any(CommentDTO.class));
    }

    @Test
    void deleteComment_WithValidId_ShouldCallServiceDelete() {
        // Arrange
        Long commentId = 1L;
        doNothing().when(commentService).deleteComment(commentId);

        // Act
        commentController.deleteComment(commentId);

        // Assert
        verify(commentService, times(1)).deleteComment(commentId);
    }
}