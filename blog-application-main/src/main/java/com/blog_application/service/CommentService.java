//package com.blog_application.service;
//
//import com.blog_application.model.Comment;
//import com.blog_application.model.Post;
//import com.blog_application.repository.CommentRepository;
//import com.blog_application.repository.PostRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class CommentService {
//
//    private final CommentRepository commentRepository;
//    private final PostRepository postRepository;
//
//    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
//        this.commentRepository = commentRepository;
//        this.postRepository = postRepository;
//    }
//
//    public Comment save(Comment comment, Long postId) {
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + postId));
//        comment.setPost(post);
//        return commentRepository.save(comment);
//    }
//
//    public List<Comment> findByPostId(Long postId) {
//        return commentRepository.findByPostId(postId);
//    }
//
//    public Comment getCommentById(Long id) {
//        return commentRepository.findById(id).orElse(null);
//    }
//
//    public void saveEditedComment(Comment comment) {
//        commentRepository.save(comment);
//    }
//
//    public void deleteComment(Long id) {
//        commentRepository.deleteById(id);
//    }
//}
//

package com.blog_application.service;

import com.blog_application.model.Comment;
import com.blog_application.model.Post;
import com.blog_application.repository.CommentRepository;
import com.blog_application.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public Comment save(Comment comment, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + postId));
        comment.setPost(post);
        if (comment.getCreatedAt() == null) {
            comment.setCreatedAt(LocalDateTime.now());
        }
        return commentRepository.save(comment);
    }

    public List<Comment> findByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElse(null);
    }

    public Comment saveEditedComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}

