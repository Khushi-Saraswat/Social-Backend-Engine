package com.grid07.assignment.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @ManyToOne
      @JoinColumn(name = "post_id")
      private Post post;

      private Long authorId;

      private String authorType;

       @Column(columnDefinition = "TEXT")
       private String content;

       private int depthLevel;

       private LocalDateTime createdAt;

        public Comment(){

        }

        public Comment(Post post, Long authorId, String authorType, String content, int depthLevel) {
            this.post = post;
            this.authorId = authorId;
            this.authorType = authorType;
            this.content = content;
            this.depthLevel = depthLevel;
            this.createdAt = LocalDateTime.now();
        }







}
