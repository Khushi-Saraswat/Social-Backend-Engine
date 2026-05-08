package com.grid07.assignment.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grid07.assignment.dto.CreateCommentRequest;
import com.grid07.assignment.dto.CreatePostRequest;
import com.grid07.assignment.entity.Comment;
import com.grid07.assignment.entity.Post;
import com.grid07.assignment.service.PostService;

@RestController
@RequestMapping("/posts")
public class PostController {

 private final PostService postService;

 public PostController(PostService postService) {
     this.postService = postService;
 }
    

    
 @PostMapping
 public Post createPost(@RequestBody CreatePostRequest request){
         return postService.createPost(request);
 }

 @PostMapping("/{postId}/comments")
 public Comment addComment(@PathVariable Long postId,@RequestBody CreateCommentRequest request){
        return postService.addComment(postId, request);
 }

 @PostMapping("/{postId}/like")
 public String likePost(@PathVariable Long postId){
       return postService.likePost(postId);
 }


}
