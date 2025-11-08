package services;

import entities.Comment;
import repositories.CommentRepository;
import java.util.List;
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
