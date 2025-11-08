package com.example.demo.services;

import com.example.demo.entities.Like;
import com.example.demo.repositories.LikeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LikeService {
    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository){
        this.likeRepository=likeRepository;
    }

    public List<Like> getAllLikes(){
        return likeRepository.findAll();
    }

    public Like getLike(Long id){
        return likeRepository.findById(id).orElseThrow(() -> new RuntimeException("Like not found"));
    }

    public Like createLike(Like like){
        return likeRepository.save(like);
    }

    public void deleteLike(Long id){
        likeRepository.deleteById(id);
    }
}