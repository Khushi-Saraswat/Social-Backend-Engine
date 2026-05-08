package com.grid07.assignment.dto;



public record CreatePostRequest (
    Long authorId,
    String authorType,
    String content
){
}
