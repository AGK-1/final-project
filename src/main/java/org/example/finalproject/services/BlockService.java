package org.example.finalproject.services;


import org.example.finalproject.dto.BlockDTO;
import org.example.finalproject.dto.BlockResponseDTO;
import org.example.finalproject.entity.Block;
import org.example.finalproject.entity.User;
import org.example.finalproject.repository.BlockRepository;
import org.example.finalproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

@Service
public class BlockService {
    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private UserRepository userRepository;

    public List<BlockResponseDTO> getAllBlocks()
    {
        List<Block> blocks = blockRepository.findAll();
        List<BlockResponseDTO> blockDTOS = new ArrayList<>();
        for(Block block : blocks){
            BlockResponseDTO dto = new BlockResponseDTO();
            dto.setBlockedId(block.getId());
            String BlockerName = block.getBlocked().getName();
            dto.setBlockerName(BlockerName);
            blockDTOS.add(dto);
        }
        return blockDTOS;
    }

    public List<BlockResponseDTO> getAllBlockedUsers(Long id)
    {
        List<Block> blocks = blockRepository.findByBlockerId(id);
        List<BlockResponseDTO> blockDTOS = new ArrayList<>();
        for(Block block : blocks){
            BlockResponseDTO dto = new BlockResponseDTO();
            dto.setBlockedId(block.getId());
            String BlockerName = block.getBlocked().getName();
            dto.setBlockerName(BlockerName);
            blockDTOS.add(dto);
        }
        return blockDTOS;
    }

    public BlockDTO createBlock(BlockDTO blockDto, Long id)
    {
        User user = userRepository.findById(id).orElseThrow(()->
                new RuntimeException("User not found"));
        User blocked = userRepository.findById(blockDto.getBlockedId())
                .orElseThrow(() -> new RuntimeException("Blocked user not found"));
        Block block =  new Block();
        block.setBlocker(user);
        block.setBlocked(blocked);
        blockRepository.save(block);
        return blockDto;
    }

    public String deleteBlock(Long blockerId, Long blockedId)
    {
        Block block = blockRepository.findByBlockerIdAndBlockedId(blockerId, blockedId)
                .orElseThrow(() -> new RuntimeException("Block not found"));

        blockRepository.delete(block);

        return "Block deleted successfully";
    }

}
