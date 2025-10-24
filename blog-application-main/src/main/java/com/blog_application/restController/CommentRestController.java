//package com.blog_application.restController;
//
//import com.blog_application.dto.responseDto.CommentResponseDto;
//import com.blog_application.model.Comment;
//import com.blog_application.service.CommentService;
//import com.blog_application.service.PostService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/comments")
//public class CommentRestController {
//
//    private final CommentService commentService;
//    private final PostService postService;
//
//    public CommentRestController(CommentService commentService, PostService postService) {
//        this.commentService = commentService;
//        this.postService = postService;
//    }
//
//    @PostMapping("/new")
//    public ResponseEntity<CommentResponseDto> saveComment(@RequestBody Comment comment, @RequestParam("postId") Long postId) {
//        commentService.save(comment, postId);
//        return ResponseEntity.ok(convertToCommentResponseDto(comment));
//    }
//
//    @GetMapping("/{id}/edit")
//    public ResponseEntity<CommentResponseDto> showEditForm(@PathVariable Long id) {
//        Comment comment = commentService.getCommentById(id);
//        return ResponseEntity.ok(convertToCommentResponseDto(comment));
//    }
//
//    @PutMapping("/{id}/update")
//    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long id, @RequestBody Comment updatedComment) {
//        Comment comment = commentService.getCommentById(id);
//        comment.setComment(updatedComment.getComment());
//        commentService.saveEditedComment(comment);
//        return ResponseEntity.ok(convertToCommentResponseDto(comment));
//    }
//
//    @DeleteMapping("/{id}/delete")
//    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
//        commentService.deleteComment(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    private CommentResponseDto convertToCommentResponseDto(Comment comment) {
//        CommentResponseDto dto = new CommentResponseDto();
//        dto.setId(comment.getId());
//        dto.setName(comment.getName());
//        dto.setEmail(comment.getEmail());
//        dto.setComment(comment.getComment());
//        dto.setCreatedAt(comment.getCreatedAt());
//        dto.setPostId(comment.getPost().getId());
//        return dto;
//    }
//}



//
//package com.blog_application.restController;
//
//import com.blog_application.dto.responseDto.CommentResponseDto;
//import com.blog_application.model.Comment;
//import com.blog_application.service.CommentService;
//import com.blog_application.service.PostService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/comments")
//public class CommentRestController {
//
//    private final CommentService commentService;
//    private final PostService postService;
//
//    public CommentRestController(CommentService commentService, PostService postService) {
//        this.commentService = commentService;
//        this.postService = postService;
//    }
//
//    @PostMapping("/new")
//    public ResponseEntity<CommentResponseDto> saveComment(@RequestBody Comment comment, @RequestParam("postId") Long postId) {
//        commentService.save(comment, postId);
//        return ResponseEntity.ok(convertToCommentResponseDto(comment));
//    }
//
//    @GetMapping("/{id}/edit")
//    public ResponseEntity<CommentResponseDto> showEditForm(@PathVariable Long id) {
//        Comment comment = commentService.getCommentById(id);
//        return ResponseEntity.ok(convertToCommentResponseDto(comment));
//    }
//
//    @PutMapping("/{id}/update")
//    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long id, @RequestBody Comment updatedComment) {
//        Comment comment = commentService.getCommentById(id);
//        comment.setComment(updatedComment.getComment());
//        commentService.saveEditedComment(comment);
//        return ResponseEntity.ok(convertToCommentResponseDto(comment));
//    }
//
//    @DeleteMapping("/{id}/delete")
//    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
//        commentService.deleteComment(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    private CommentResponseDto convertToCommentResponseDto(Comment comment) {
//        CommentResponseDto dto = new CommentResponseDto();
//        dto.setId(comment.getId());
//        dto.setName(comment.getName());
//        dto.setEmail(comment.getEmail());
//        dto.setComment(comment.getComment());
//        dto.setCreatedAt(comment.getCreatedAt());
//        dto.setPostId(comment.getPost().getId());
//        return dto;
//    }
//}





//package com.blog_application.restController;
//import com.blog_application.dto.responseDto.CommentResponseDto;
//import com.blog_application.model.Comment;
//import com.blog_application.model.Post;
//import com.blog_application.model.User;
//import com.blog_application.service.CommentService;
//import com.blog_application.service.CustomUserDetailsService;
//import com.blog_application.service.PostService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/comments")
//public class CommentRestController {
//
//    private final CommentService commentService;
//    private final PostService postService;
//    private final CustomUserDetailsService userDetailsService;
//
//    @Autowired
//    public CommentRestController(CommentService commentService, PostService postService,
//                                 CustomUserDetailsService userDetailsService) {
//        this.commentService = commentService;
//        this.postService = postService;
//        this.userDetailsService = userDetailsService;
//    }
//
//    @GetMapping("/post/{postId}")
//    public ResponseEntity<List<CommentResponseDto>> getCommentsByPostId(@PathVariable Long postId) {
//        List<Comment> comments = commentService.findByPostId(postId);
//        List<CommentResponseDto> commentDtos = comments.stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//
//        return new ResponseEntity<>(commentDtos, HttpStatus.OK);
//    }
//
//    @PostMapping("/new")
//    public ResponseEntity<CommentResponseDto> createComment(
//            @RequestBody Comment comment,
//            @RequestParam("postId") Long postId) {
//
//        Comment savedComment = commentService.save(comment, postId);
//        return new ResponseEntity<>(convertToDto(savedComment), HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{id}/update")
//    public ResponseEntity<CommentResponseDto> updateComment(
//            @PathVariable Long id,
//            @RequestBody Comment updatedComment,
//            Principal principal) {
//
//        Comment existingComment = commentService.getCommentById(id);
//        if (existingComment == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        // Check if current user is comment owner or admin
//        String email = principal.getName();
//        User user = userDetailsService.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (!existingComment.getEmail().equals(user.getEmail()) &&
//                !user.getRole().equals("ADMIN")) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//
//        existingComment.setComment(updatedComment.getComment());
//        Comment savedComment = commentService.saveEditedComment(existingComment);
//
//        return new ResponseEntity<>(convertToDto(savedComment), HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}/delete")
//    public ResponseEntity<Void> deleteComment(@PathVariable Long id, Principal principal) {
//        Comment existingComment = commentService.getCommentById(id);
//        if (existingComment == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        // Check if current user is comment owner or admin
//        String email = principal.getName();
//        User user = userDetailsService.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (!existingComment.getEmail().equals(user.getEmail()) &&
//                !user.getRole().equals("ADMIN")) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//
//        commentService.deleteComment(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//    private CommentResponseDto convertToDto(Comment comment) {
//        CommentResponseDto dto = new CommentResponseDto();
//        dto.setId(comment.getId());
//        dto.setName(comment.getName());
//        dto.setEmail(comment.getEmail());
//        dto.setComment(comment.getComment());
//        dto.setCreatedAt(comment.getCreatedAt());
//
//        if (comment.getPost() != null) {
//            dto.setPostId(comment.getPost().getId());
//        }
//
//        return dto;
//    }
//}


//
//package com.blog_application.restController;
//
//import com.blog_application.dto.responseDto.CommentResponseDto;
//import com.blog_application.model.Comment;
//import com.blog_application.model.Post;
//import com.blog_application.model.User;
//import com.blog_application.service.CommentService;
//import com.blog_application.service.CustomUserDetailsService;
//import com.blog_application.service.PostService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import java.security.Principal;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/comments")
//public class CommentRestController {
//
//    private final CommentService commentService;
//    private final PostService postService;
//    private final CustomUserDetailsService userDetailsService;
//
//    @Autowired
//    public CommentRestController(CommentService commentService, PostService postService,
//                                 CustomUserDetailsService userDetailsService) {
//        this.commentService = commentService;
//        this.postService = postService;
//        this.userDetailsService = userDetailsService;
//    }
//
//    @GetMapping("/post/{postId}")
//    public ResponseEntity<List<CommentResponseDto>> getCommentsByPostId(@PathVariable Long postId) {
//        List<Comment> comments = commentService.findByPostId(postId);
//        List<CommentResponseDto> commentDtos = comments.stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//
//        return new ResponseEntity<>(commentDtos, HttpStatus.OK);
//    }
//
//    @PostMapping("/new")
//    public ResponseEntity<CommentResponseDto> createComment(
//            @RequestBody Comment comment,
//            @RequestParam("postId") Long postId) {
//
//        Comment savedComment = commentService.save(comment, postId);
//        return new ResponseEntity<>(convertToDto(savedComment), HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{id}/update")
//    public ResponseEntity<CommentResponseDto> updateComment(
//            @PathVariable Long id,
//            @RequestBody Comment updatedComment,
//            Principal principal) {
//
//        Comment existingComment = commentService.getCommentById(id);
//        if (existingComment == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        String email = principal.getName();
//        User user = userDetailsService.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (!isAuthorizedToManageComment(existingComment, user)) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//
//        existingComment.setComment(updatedComment.getComment());
//        Comment savedComment = commentService.saveEditedComment(existingComment);
//
//        return new ResponseEntity<>(convertToDto(savedComment), HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}/delete")
//    public ResponseEntity<Void> deleteComment(@PathVariable Long id, Principal principal) {
//        Comment existingComment = commentService.getCommentById(id);
//        if (existingComment == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//
//        String email = principal.getName();
//        User user = userDetailsService.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (!isAuthorizedToManageComment(existingComment, user)) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
//        }
//
//        commentService.deleteComment(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//    private boolean isAuthorizedToManageComment(Comment comment, User user) {
//        Post post = comment.getPost();
//        if (post == null) {
//            return false;
//        }
//
//        // Check if user is admin or post author
//        boolean isAdmin = user.getRole() != null && user.getRole().toUpperCase().contains("ADMIN");
//        boolean isPostAuthor = post.getAuthor() != null && post.getAuthor().getId().equals(user.getId());
//
//        return isAdmin || isPostAuthor;
//    }
//
//    private CommentResponseDto convertToDto(Comment comment) {
//        CommentResponseDto dto = new CommentResponseDto();
//        dto.setId(comment.getId());
//        dto.setName(comment.getName());
//        dto.setEmail(comment.getEmail());
//        dto.setComment(comment.getComment());
//        dto.setCreatedAt(comment.getCreatedAt());
//
//        if (comment.getPost() != null) {
//            dto.setPostId(comment.getPost().getId());
//        }
//
//        return dto;
//    }
//}

package com.blog_application.restController;

import com.blog_application.dto.responseDto.CommentResponseDto;
import com.blog_application.model.Comment;
import com.blog_application.model.Post;
import com.blog_application.model.User;
import com.blog_application.service.CommentService;
import com.blog_application.service.CustomUserDetailsService;
import com.blog_application.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    private final CommentService commentService;
    private final PostService postService;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public CommentRestController(CommentService commentService, PostService postService,
                                 CustomUserDetailsService userDetailsService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPostId(@PathVariable Long postId) {
        List<Comment> comments = commentService.findByPostId(postId);
        List<CommentResponseDto> commentDtos = comments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(commentDtos, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<CommentResponseDto> createComment(
            @RequestBody Comment comment,
            @RequestParam("postId") Long postId) {

        Comment savedComment = commentService.save(comment, postId);
        return new ResponseEntity<>(convertToDto(savedComment), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long id,
            @RequestBody Comment updatedComment,
            Principal principal) {

        Comment existingComment = commentService.getCommentById(id);
        if (existingComment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String email = principal.getName();
        User user = userDetailsService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!isAuthorizedToManageComment(existingComment, user)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        existingComment.setComment(updatedComment.getComment());
        Comment savedComment = commentService.saveEditedComment(existingComment);

        return new ResponseEntity<>(convertToDto(savedComment), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id, Principal principal) {
        Comment existingComment = commentService.getCommentById(id);
        if (existingComment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String email = principal.getName();
        User user = userDetailsService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!isAuthorizedToManageComment(existingComment, user)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        commentService.deleteComment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private boolean isAuthorizedToManageComment(Comment comment, User user) {
        Post post = comment.getPost();
        if (post == null) {
            return false;
        }

        // Check authorities using Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ADMIN") || auth.getAuthority().equals("ROLE_ADMIN"));

        // Check if user is the post author
        boolean isPostAuthor = post.getAuthor() != null && post.getAuthor().getId().equals(user.getId());

        return isAdmin || isPostAuthor;
    }

    private CommentResponseDto convertToDto(Comment comment) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setId(comment.getId());
        dto.setName(comment.getName());
        dto.setEmail(comment.getEmail());
        dto.setComment(comment.getComment());
        dto.setCreatedAt(comment.getCreatedAt());

        if (comment.getPost() != null) {
            dto.setPostId(comment.getPost().getId());
        }

        return dto;
    }
}