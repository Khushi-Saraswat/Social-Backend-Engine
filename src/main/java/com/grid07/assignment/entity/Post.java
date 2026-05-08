package com.grid07.assignment.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "posts")
@Entity
public class Post {
    

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     private Long authorId;

     private String authorType;

     @Column(columnDefinition = "TEXT")
     private String content;

     private LocalDateTime createdAt;

     private int likeCount=0;

     public Post() {

    }

    public Post(Long authorId, String authorType, String content) {
            this.authorId = authorId;
            this.authorType = authorType;
            this.content = content;
            this.createdAt = LocalDateTime.now();
    }







}
