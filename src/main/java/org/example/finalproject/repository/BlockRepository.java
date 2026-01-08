package org.example.finalproject.repository;

import org.example.finalproject.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BlockRepository extends JpaRepository <Block, Long>{
    List<Block> findByBlockerId(Long blockerId);
    Optional<Block> findByBlockerIdAndBlockedId(Long blockerId, Long blockedId);


    @Query("SELECT b.blocker.id FROM Block b WHERE b.blocked.id = :userId")
    List<Long> findBlockerIdsByBlockedId(@Param("userId") Long userId);

    @Query("SELECT b.blocked.id FROM Block b WHERE b.blocker.id = :userId")
    List<Long> findBlockedIdsByBlockerId(@Param("userId") Long userId);
}
