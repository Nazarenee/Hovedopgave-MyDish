package services;

import entities.Like;
import repositories.LikeRepository;
import java.util.List;
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