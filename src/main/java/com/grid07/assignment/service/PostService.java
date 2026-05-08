package com.grid07.assignment.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.grid07.assignment.dto.CreateCommentRequest;
import com.grid07.assignment.dto.CreatePostRequest;
import com.grid07.assignment.entity.Comment;
import com.grid07.assignment.entity.Post;
import com.grid07.assignment.exception.BotLimitExceededException;
import com.grid07.assignment.exception.ContentExceedException;
import com.grid07.assignment.exception.PostNotFound;
import com.grid07.assignment.repository.CommentRepository;
import com.grid07.assignment.repository.PostRepository;

import jakarta.transaction.Transactional;

@Service
public class PostService {
    
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final RedisService redisService;
    private final NotificationService notificationService;

    public PostService(PostRepository postRepository, CommentRepository commentRepository,RedisService redisService, NotificationService notificationService) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.redisService = redisService;
        this.notificationService = notificationService;
    }


    public Post createPost(CreatePostRequest request){
        var post = new Post();
        post.setAuthorId(request.authorId());
        post.setAuthorType(request.authorType());
        post.setContent(request.content());
        post.setCreatedAt(LocalDateTime.now());

        return postRepository.save(post);

    }


    @Transactional
    public Comment addComment(Long postId,
                          CreateCommentRequest request) {

    var post = postRepository.findById(postId)
            .orElseThrow(() ->
                    new PostNotFound("Post not found with id: " + postId));

    // Vertical Cap
    if (request.depthLevel() > 20) {

        throw new ContentExceedException(
                "Maximum comment depth exceeded");
    }

    // Bot validations
    if (request.authorType().equals("BOT")) {

        Long botCount =
                redisService.increaseBotCount(postId);

        if (botCount > 100) {

            throw new BotLimitExceededException(
                    "Bot comment limit exceeded for this post");
        }

        Long humanId = 1L;

        if (redisService.hasCooldown(
                request.authorId(),
                humanId)) {

            throw new BotLimitExceededException(
                    "Cooldown active for this bot");
        }

        redisService.createCooldown(
                request.authorId(),
                humanId);
    }

    var comment = new Comment();

    comment.setPost(post);
    comment.setAuthorId(request.authorId());
    comment.setAuthorType(request.authorType());
    comment.setContent(request.content());
    comment.setDepthLevel(request.depthLevel());
    comment.setCreatedAt(LocalDateTime.now());

    Comment savedComment =
            commentRepository.save(comment);

    // Virality updates AFTER save
    if (request.authorType().equals("BOT")) {

        redisService.increaseViralityScore(
                postId,
                1);
    }

    if (request.authorType().equals("USER")) {

        redisService.increaseViralityScore(
                postId,
                50);
    }

    if(request.authorType().equals("BOT")){
      notificationService.handleBotNotification(
        1L,
        "Bot " + request.authorId()
        + " replied to your post"
    );
    }

    return savedComment;
    }

    public String likePost(Long postId){
        var post = postRepository.findById(postId).orElseThrow(
            () -> new PostNotFound("Post not found with id: " + postId)
        );
        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);
        redisService.increaseViralityScore(postId,20);
        return "Post liked successfully";
    }


}
