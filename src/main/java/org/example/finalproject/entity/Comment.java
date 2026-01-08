package org.example.finalproject.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    //private String author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id")
    private Posts commentPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_table_id")
    private User authorUser;


    private LocalDateTime date = LocalDateTime.now();

}
