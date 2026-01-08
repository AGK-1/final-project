package org.example.finalproject.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user_blocks")
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "blocker_id", nullable = false)
    private User blocker; // Кто заблокировал

    @ManyToOne
    @JoinColumn(name = "blocked_id", nullable = false)
    private User blocked; // Кого заблокировали

    @Column(name = "blocked_at")
    private LocalDateTime blockedAt = LocalDateTime.now();;

}

