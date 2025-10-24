
package com.blog_application.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.blog_application.model.Comment;
import com.blog_application.model.Post;
import com.blog_application.model.User;
import com.blog_application.service.CommentService;
import com.blog_application.service.CustomUserDetailsService;
import com.blog_application.service.PostService;
import com.blog_application.service.TagService;

@Controller
public class PostController {

    private final PostService postService;
    private final TagService tagService;
    private final CommentService commentService;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public PostController(PostService postService, TagService tagService, CommentService commentService, CustomUserDetailsService userDetailsService) {
        this.postService = postService;
        this.tagService = tagService;
        this.commentService = commentService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/")
    public String listPosts(
            @RequestParam(defaultValue = "") String searchTerm,
            @RequestParam(required = false) LocalDateTime fromDate,
            @RequestParam(required = false) LocalDateTime toDate,
            @RequestParam(required = false) List<String> authors,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "publishedAt") String sort,
            @RequestParam(defaultValue = "desc", name = "sortDir") String sortDir,
            Model model) {
        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by(Sort.Direction.fromString(sortDir), sort)
        );
        if (authors != null && authors.isEmpty()) {
            authors = null;
        }
        if (tags != null && tags.isEmpty()) {
            tags = null;
        }
        Page<Post> posts = postService.findPosts(
                searchTerm,
                fromDate,
                toDate,
                authors,
                tags,
                pageable
        );
        model.addAttribute("posts", posts.getContent());
        model.addAttribute("currentPage", posts.getNumber() + 1);
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("authors", authors);
        model.addAttribute("tags", tags);
        model.addAttribute("sort", sort);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("allAuthors", postService.getAllAuthors());
        model.addAttribute("allTags", tagService.getAllTags());
        return "postList";
    }

    @GetMapping("/newpost")
    public String newPostForm(Model model) {
        model.addAttribute("post", new Post());
        return "create_post";
    }

    @PostMapping("/newpost")
    public String createPost(@ModelAttribute Post post, Principal principal) {
        String email = principal.getName();
        User user = userDetailsService.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        post.setAuthor(user);
        postService.savePost(post);
        return "redirect:/";
    }

    @GetMapping("/post/{id}/edit")
    public String editPostForm(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        String tags = post.getPostTags().stream()
                .map(postTag -> postTag.getTag().getName())
                .collect(Collectors.joining(", "));
        post.setTagString(tags);
        model.addAttribute("post", post);
        model.addAttribute("tags", tagService.getAllTags());
        return "create_post";
    }

    @PostMapping("/post/{id}/edit")
    public String updatePost(@PathVariable Long id, @ModelAttribute Post post, Principal principal) {
        String email = principal.getName();
        User user = userDetailsService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        post.setId(id);
        post.setAuthor(user);
        postService.updatePost(id, post);

        return "redirect:/post/" + id;
    }

    @GetMapping("/post/{id}")
    public String displayPost(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        List<Comment> comments = commentService.findByPostId(id);

        String tags = post.getPostTags().stream()
                .map(postTag -> postTag.getTag().getName())
                .collect(Collectors.joining(", "));
        post.setTagString(tags);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication.getPrincipal() instanceof String)) {
            User user = userDetailsService.getUserByEmail(authentication.getName());
            model.addAttribute("loggedInUser", user);
        }

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("newComment", new Comment());
        model.addAttribute("postAuthorEmail", post.getAuthor().getEmail());

        return "postDetails";
    }

    @PostMapping("/post/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/";
    }
}