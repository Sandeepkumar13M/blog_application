package com.blog_application.controller;

import com.blog_application.service.PostService;
import com.blog_application.model.Comment;
import com.blog_application.service.CommentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @PostMapping("/comment/new")
    public String saveComment(@ModelAttribute(name = "newComment") Comment comment, @RequestParam("postId") Long postId) {
        Comment newComment = new Comment();
        commentService.save(comment, postId);
        return "redirect:/post/" + postId;
    }

    @GetMapping("/comment/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Comment comment = commentService.getCommentById(id);
        model.addAttribute("comment", comment);
        return "editComments";
    }

    @PostMapping("/comment/{id}/update")
    public String updateComment(@PathVariable Long id, @ModelAttribute("comments") Comment updatedComment) {
        Comment comment = commentService.getCommentById(id);
        comment.setComment(updatedComment.getComment());
        commentService.saveEditedComment(comment);
        Long postId = comment.getPost().getId();
        return "redirect:/post/" + postId;
    }

    @PostMapping("/comment/{id}/delete")
    public String deleteComment(@PathVariable Long id) {
        Comment comment = commentService.getCommentById(id);
        Long postId = comment.getPost().getId();
        commentService.deleteComment(id);
        return "redirect:/post/" + postId;
    }
}

