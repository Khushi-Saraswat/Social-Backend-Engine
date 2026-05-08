package com.grid07.assignment.dto;



public record CreateCommentRequest (
    Long authorId,
    String authorType,
    String content,
    int depthLevel
){
}
