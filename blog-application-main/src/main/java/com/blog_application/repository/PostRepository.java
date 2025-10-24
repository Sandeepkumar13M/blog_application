
package com.blog_application.repository;

import com.blog_application.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN p.postTags pt " +
            "LEFT JOIN pt.tag t " +
            "WHERE (:searchTerm IS NULL OR :searchTerm = '' OR " +
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.content) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "AND p.publishedAt BETWEEN :fromDate AND :toDate " +
            "AND (:authors IS NULL OR p.author.name IN :authors) " +
            "AND (:tags IS NULL OR t.name IN :tags)")
    Page<Post> findPosts(
            @Param("searchTerm") String searchTerm,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            @Param("authors") List<String> authors,
            @Param("tags") List<String> tags,
            Pageable pageable
    );

    @Query("SELECT DISTINCT p.author.name FROM Post p")
    List<String> findDistinctAuthors();
}