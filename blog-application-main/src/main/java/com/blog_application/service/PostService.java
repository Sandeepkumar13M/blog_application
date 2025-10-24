
package com.blog_application.service;

import com.blog_application.model.*;
import com.blog_application.repository.PostRepository;
import com.blog_application.repository.PostTagsRepository;
import com.blog_application.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final TagService tagService;
    private final TagRepository tagRepository;
    private final PostTagsRepository postTagsRepository;

    @Autowired
    public PostService(PostRepository postRepository, TagService tagService, TagRepository tagRepository,
                       PostTagsRepository postTagsRepository) {
        this.postRepository = postRepository;
        this.tagService = tagService;
        this.tagRepository = tagRepository;
        this.postTagsRepository = postTagsRepository;
    }

    @Transactional
    public Post savePost(Post post) {
        LocalDateTime now = LocalDateTime.now();
        post.setCreatedAt(now);
        post.setPublishedAt(now);
        post.setUpdatedAt(now);
        post.setPublished(true);

        Post savedPost = postRepository.save(post);
        processTags(savedPost);
        return savedPost;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Transactional
    public Post updatePost(Long id, Post updatedPost) {
        Post post = getPostById(id);
        if (post != null) {
            post.setTitle(updatedPost.getTitle());
            post.setContent(updatedPost.getContent());
            post.setExcerpt(updatedPost.getExcerpt());
            post.setAuthor(updatedPost.getAuthor());
            post.setUpdatedAt(LocalDateTime.now());
            post.setTagString(updatedPost.getTagString());

            postTagsRepository.deleteByPostId(id);
            processTags(post);

            return postRepository.save(post);

        }
        return postRepository.save(post);
    }

    public Page<Post> getAllPostsPaginated(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    private void processTags(Post post) {
        postTagsRepository.deleteByPostId(post.getId());

        if (post.getTagString() != null && !post.getTagString().isEmpty()) {
            List<Tag> tags = tagService.processTagString(post.getTagString());
            for (Tag tag : tags) {
                Tag savedTag = tagRepository.findByName(tag.getName())
                        .orElseGet(() -> tagRepository.save(tag));

                PostTags postTags = new PostTags();
                postTags.setPost(post);
                postTags.setTag(savedTag);
                postTags.setCreatedAt(LocalDateTime.now());
                postTags.setUpdatedAt(LocalDateTime.now());
                postTagsRepository.save(postTags);
            }
        }
    }

    public Page<Post> findPosts(
            String searchTerm,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            List<String> authors,
            List<String> tags,
            Pageable pageable) {
        fromDate = (fromDate == null) ? LocalDateTime.of(2000, 1, 1, 0, 0) : fromDate;
        toDate = (toDate == null) ? LocalDateTime.now() : toDate;
        return postRepository.findPosts(
                searchTerm,
                fromDate,
                toDate,
                authors,
                tags,
                pageable
        );
    }

    public List<String> getAllAuthors() {
        return postRepository.findDistinctAuthors();
    }
}