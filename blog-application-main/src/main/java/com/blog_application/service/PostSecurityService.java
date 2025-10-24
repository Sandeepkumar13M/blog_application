package com.blog_application.service;

import com.blog_application.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostSecurityService {

    @Autowired
    private PostService postService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    public boolean isPostAuthor(Long postId, String email) {
        Post post = postService.getPostById(postId);
        if (post == null || post.getAuthor() == null) {
            return false;
        }
        return post.getAuthor().getEmail().equals(email);
    }
}