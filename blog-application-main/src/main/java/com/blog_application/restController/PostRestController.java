
package com.blog_application.restController;

import com.blog_application.dto.responseDto.PostResponseDto;
import com.blog_application.model.Post;
import com.blog_application.model.User;
import com.blog_application.service.CustomUserDetailsService;
import com.blog_application.service.PostService;
import com.blog_application.service.PostSecurityService;
import com.blog_application.service.TagService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    private static final Logger logger = LoggerFactory.getLogger(PostRestController.class);
    private static final String ADMIN_ROLE = "ADMIN"; // Match database role

    private final PostService postService;
    private final CustomUserDetailsService userDetailsService;
    private final TagService tagService;
    private final PostSecurityService postSecurityService;

    @Autowired
    public PostRestController(PostService postService, CustomUserDetailsService userDetailsService, TagService tagService, PostSecurityService postSecurityService) {
        this.postService = postService;
        this.userDetailsService = userDetailsService;
        this.tagService = tagService;
        this.postSecurityService = postSecurityService;
    }

    /**
     * Get all posts with filtering and pagination
     */
//    @GetMapping
//    public ResponseEntity<Map<String, Object>> getAllPosts(
//            @RequestParam(defaultValue = "") String searchTerm,
//            @RequestParam(required = false) LocalDateTime fromDate,
//            @RequestParam(required = false) LocalDateTime toDate,
//            @RequestParam(required = false) List<String> authors,
//            @RequestParam(required = false) List<String> tags,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "publishedAt") String sort,
//            @RequestParam(defaultValue = "desc") String sortDir) {
//
//        try {
//            // Normalize empty lists to null for proper handling
//            if (authors != null && authors.isEmpty()) {
//                authors = null;
//            }
//            if (tags != null && tags.isEmpty()) {
//                tags = null;
//            }
//
//            // Create pageable object
//            Pageable pageable = PageRequest.of(
//                    page, size,
//                    Sort.by(Sort.Direction.fromString(sortDir), sort)
//            );
//
//            // Get posts with pagination
//            Page<Post> postsPage = postService.findPosts(
//                    searchTerm, fromDate, toDate, authors, tags, pageable);
//
//            // Convert posts to DTOs
//            List<PostResponseDto> postDtos = postsPage.getContent().stream()
//                    .map(this::convertToDto)
//                    .collect(Collectors.toList());
//
//            // Prepare response with pagination info
//            Map<String, Object> response = new HashMap<>();
//            response.put("posts", postDtos);
//            response.put("currentPage", postsPage.getNumber());
//            response.put("totalItems", postsPage.getTotalElements());
//            response.put("totalPages", postsPage.getTotalPages());
//
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } catch (Exception e) {
//            logger.error("Error retrieving posts: {}", e.getMessage(), e);
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("message", "Error retrieving posts: " + e.getMessage());
//            errorResponse.put("status", "error");
//            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPosts(
            @RequestParam(defaultValue = "") String searchTerm,
            @RequestParam(required = false) LocalDateTime fromDate,
            @RequestParam(required = false) LocalDateTime toDate,
            @RequestParam(required = false) List<String> authors,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "publishedAt") String sort,
            @RequestParam(defaultValue = "desc") String sortDir) {

        try {
            // Normalize empty lists to null for proper handling
            if (authors != null && authors.isEmpty()) {
                authors = null;
            }
            if (tags != null && tags.isEmpty()) {
                tags = null;
            }

            // Create pageable object
            Pageable pageable = PageRequest.of(
                    page, size,
                    Sort.by(Sort.Direction.fromString(sortDir), sort)
            );

            // Get posts with pagination
            Page<Post> postsPage = postService.findPosts(
                    searchTerm, fromDate, toDate, authors, tags, pageable);

            // Convert posts to DTOs
            List<PostResponseDto> postDtos = postsPage.getContent().stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            // Prepare response with pagination info
            Map<String, Object> response = new HashMap<>();
            response.put("posts", postDtos);
            response.put("currentPage", postsPage.getNumber());
            response.put("totalItems", postsPage.getTotalElements());
            response.put("totalPages", postsPage.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving posts: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error retrieving posts: " + e.getMessage());
            errorResponse.put("status", "error");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get a specific post by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        try {
            Post post = postService.getPostById(id);
            if (post == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "Post not found with id: " + id);
                errorResponse.put("status", "not_found");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(convertToDto(post), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving post: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error retrieving post: " + e.getMessage());
            errorResponse.put("status", "error");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Create a new post
     */
    @PostMapping("/newpost")
    @PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR')")
    public ResponseEntity<?> createPost(@RequestBody Post post, Principal principal) {
        try {
            if (principal == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "Error authenticating user");
                errorResponse.put("status", "unauthorized");
                return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
            }

            String email = principal.getName();
            User user = userDetailsService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            post.setAuthor(user);
            post.setCreatedAt(LocalDateTime.now());
            post.setUpdatedAt(LocalDateTime.now());
            if (post.isPublished() && post.getPublishedAt() == null) {
                post.setPublishedAt(LocalDateTime.now());
            }

            Post savedPost = postService.savePost(post);

            return new ResponseEntity<>(convertToDto(savedPost), HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating post: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error creating post: " + e.getMessage());
            errorResponse.put("status", "error");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update an existing post
     */
    @PutMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR') and @postSecurityService.isPostAuthor(#id, authentication.name)")
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @RequestBody Post post,
            Principal principal) {
        try {
            // Verify post exists
            Post existingPost = postService.getPostById(id);
            if (existingPost == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "Post not found with id: " + id);
                errorResponse.put("status", "not_found");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }

            // Preserve important fields
            post.setId(id);
            post.setAuthor(existingPost.getAuthor());
            post.setCreatedAt(existingPost.getCreatedAt());
            post.setUpdatedAt(LocalDateTime.now());

            // Handle publication status changes
            if (!existingPost.isPublished() && post.isPublished()) {
                post.setPublishedAt(LocalDateTime.now());
            } else if (existingPost.isPublished()) {
                post.setPublishedAt(existingPost.getPublishedAt());
            }

            Post updatedPost = postService.updatePost(id, post);

            return new ResponseEntity<>(convertToDto(updatedPost), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error updating post: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error updating post: " + e.getMessage());
            errorResponse.put("status", "error");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a post
     */
    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR') and @postSecurityService.isPostAuthor(#id, authentication.name)")
    public ResponseEntity<?> deletePost(@PathVariable Long id, Principal principal) {
        try {
            // Verify post exists
            Post existingPost = postService.getPostById(id);
            if (existingPost == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "Post not found with id: " + id);
                errorResponse.put("status", "not_found");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }

            postService.deletePost(id);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Post deleted successfully");
            response.put("status", "success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error deleting post: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error deleting post: " + e.getMessage());
            errorResponse.put("status", "error");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Helper method to convert a Post entity to PostResponseDto
     */
    private PostResponseDto convertToDto(Post post) {
        try {
            PostResponseDto dto = new PostResponseDto();
            dto.setId(post.getId());
            dto.setTitle(post.getTitle() != null ? post.getTitle() : "");
            dto.setContent(post.getContent() != null ? post.getContent() : "");
            dto.setExcerpt(post.getExcerpt() != null ? post.getExcerpt() : "");
            dto.setPublishedAt(post.getPublishedAt());
            dto.setPublished(post.isPublished());
            dto.setCreatedAt(post.getCreatedAt());
            dto.setUpdatedAt(post.getUpdatedAt());

            // Handle author information
            if (post.getAuthor() != null) {
                dto.setAuthorName(post.getAuthor().getName());
                dto.setAuthorEmail(post.getAuthor().getEmail());
            } else {
                dto.setAuthorName("Unknown");
                dto.setAuthorEmail("");
            }

            // Handle tags
            if (post.getPostTags() != null) {
                dto.setTags(post.getPostTags().stream()
                        .filter(postTag -> postTag != null && postTag.getTag() != null)
                        .map(postTag -> postTag.getTag().getName())
                        .collect(Collectors.toList()));
            }

            return dto;
        } catch (Exception e) {
            logger.error("Error converting post to DTO: {}", e.getMessage(), e);
            throw new RuntimeException("Error converting post to DTO: " + e.getMessage());
        }
    }
}
