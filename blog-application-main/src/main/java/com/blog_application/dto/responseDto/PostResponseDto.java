package com.blog_application.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String excerpt;
    private LocalDateTime publishedAt;
    private boolean published;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String authorName;
    private String authorEmail;
    private List<String> tags;
    private List<CommentResponseDto> comments;
}