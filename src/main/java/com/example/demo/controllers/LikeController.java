package com.example.demo.controllers;

import com.example.demo.DTO.LikeDTO;
import com.example.demo.services.LikeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService){
        this.likeService = likeService;
    }

    @GetMapping
    public List<LikeDTO> getAllLikes(){
        return likeService.getAllLikes();
    }

    @GetMapping("/{id}")
    public LikeDTO getLike(@PathVariable Long id){
        return likeService.getLike(id);
    }

    @DeleteMapping("/recipe/{recipeId}/user/{userId}")
    public void deleteLikeByRecipeAndUser(@PathVariable Long recipeId, @PathVariable Long userId) {
        likeService.deleteLikeByRecipeAndUser(recipeId, userId);
    }

    @PostMapping
    public LikeDTO createLike(@RequestBody LikeDTO likeDTO){
        return likeService.createLike(likeDTO);
    }

    @DeleteMapping
    public void deleteLike(@PathVariable Long id){
        likeService.deleteLike(id);
    }
}
