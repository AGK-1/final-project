package org.example.finalproject.dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.example.finalproject.entity.User;


@Getter
@Setter
public class BlockDTO {
    private Long blockedId;

}

