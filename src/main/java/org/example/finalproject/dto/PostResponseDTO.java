package org.example.finalproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponseDTO {
    private Long id;
    private String content;

    private String imageUrl;
}
