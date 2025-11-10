package com.example.demo.controllers;

import com.example.demo.DTO.CommentDTO;
import com.example.demo.services.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentDTO> getAllComments(){
        return commentService.getAllComments();
    }

    @GetMapping("/{id}")
    public CommentDTO getComment(@PathVariable Long id){
        return commentService.getComment(id);
    }

    @PostMapping
    public CommentDTO createComment(@RequestBody CommentDTO commentDTO){
        return commentService.createComment(commentDTO);
    }

    @DeleteMapping
    public void deleteComment(@PathVariable Long id){
        commentService.deleteComment(id);
    }
}
