package org.example.finalproject.controllers;


import org.example.finalproject.dto.BlockDTO;
import org.example.finalproject.dto.BlockResponseDTO;
import org.example.finalproject.entity.Block;
import org.example.finalproject.entity.User;
import org.example.finalproject.repository.BlockRepository;
import org.example.finalproject.services.BlockService;
import org.example.finalproject.utils.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/block")
public class BlockController {
    private final BlockService blockService;
    private BlockRepository blockRepository;

    private final JwtUtil jwtUtil;

    public BlockController(JwtUtil jwtUtil, BlockService blockService) {
        this.jwtUtil = jwtUtil;
        this.blockService = blockService;
    }

    @GetMapping("/get-all-blocks")
    public List<BlockResponseDTO> getBlocks() {
        return blockService.getAllBlocks();
    }

    @GetMapping("/get-all-your-blockeds")
    public List<BlockResponseDTO> getAllBlockedUsers(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return blockService.getAllBlockedUsers(user.getId());
    }

    @PostMapping("/block-user")
    public BlockDTO saveBlock(@RequestBody BlockDTO blockDto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return blockService.createBlock(blockDto, user.getId());
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBlock(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        blockService.deleteBlock(user.getId(), id);
    }
}
