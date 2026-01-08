package org.example.finalproject.repository;

import org.example.finalproject.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Posts, Long> {
    @Query("""
            SELECT p FROM Posts p 
            WHERE p.user.id NOT IN (
                SELECT b.blocked.id FROM Block b WHERE b.blocker.id = :userId
            )
            """)
    List<Posts> findAllVisiblePosts(Long userId);

    List<Posts> findByUserId(Long userId);

}
