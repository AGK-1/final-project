package org.example.finalproject.repository;

import org.example.finalproject.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndAuthorUserId(Long commentId, Long userId);

    @Query("""
                SELECT c FROM Comment c
                WHERE c.authorUser.id NOT IN (
                    SELECT b.blocked.id FROM Block b WHERE b.blocker.id = :userId
                )
            """)
    List<Comment> findAllVisibleComments(@Param("userId") Long userId);
}
