package com.blog_application.dto.responseDto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponseDto {
    private Long id;
    private String name;
    private String email;
    private String comment;
    private LocalDateTime createdAt;
    private Long postId;
}