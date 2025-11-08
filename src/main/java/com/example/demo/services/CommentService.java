package com.example.demo.services;

import com.example.demo.entities.Comment;
import com.example.demo.repositories.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository){
        this.commentRepository=commentRepository;
    }


    public List<Comment> getAllComments(){
        return commentRepository.findAll();
    }

    public Comment getComment(Long id){
        return commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found"));
    }

    public Comment createComment(Comment comment){
        return commentRepository.save(comment);
    }

    public void deleteComment(Long id){
        commentRepository.deleteById(id);
    }
}
