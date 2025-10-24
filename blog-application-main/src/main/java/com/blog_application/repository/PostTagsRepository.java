package com.blog_application.repository;

import com.blog_application.model.PostTags;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostTagsRepository extends JpaRepository<PostTags, Long> {
    @Transactional
    void deleteByPostId(Long postId);
}
