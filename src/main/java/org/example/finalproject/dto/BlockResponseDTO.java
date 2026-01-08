package org.example.finalproject.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.finalproject.entity.User;

@Getter
@Setter
public class BlockResponseDTO {
    private Long blockedId;
    private String blockerName;
}

