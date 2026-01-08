package org.example.finalproject.services;

import org.example.finalproject.dto.PostDTO;
import org.example.finalproject.dto.PostResponseDTO;
import org.example.finalproject.entity.Posts;
import org.example.finalproject.entity.User;
import org.example.finalproject.repository.BlockRepository;
import org.example.finalproject.repository.PostRepository;
import org.example.finalproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    public List<PostResponseDTO> getAllPosts() {
        List<Posts> posts = postRepository.findAll();
        List<PostResponseDTO> postResponseDTOS = new ArrayList<>();
        for (Posts post : posts) {
            PostResponseDTO dto = new PostResponseDTO();
            dto.setId(post.getId());
            dto.setContent(post.getContent());
            dto.setImageUrl(post.getImageUrl());
            postResponseDTOS.add(dto);
        }
        return postResponseDTOS;
    }

    public PostResponseDTO getPostById(@PathVariable Long id) {
        Posts post = postRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Post not found with that id"));
        PostResponseDTO dto = new PostResponseDTO();
        dto.setId(post.getId());
        dto.setContent(post.getContent());
        dto.setImageUrl(post.getImageUrl());
        return dto;
    }

    public List<PostResponseDTO> getPostYourNotBlocked(Long id) {
        List<PostResponseDTO> postResponseDTOS = new ArrayList<>();
        List<Long>blockedMeUsers = blockRepository.findBlockerIdsByBlockedId(id);
        List<Posts> posts = postRepository.findAll();
        for (Posts post : posts) {
            Long postOwnerId = post.getUser().getId();

            // если владелец поста меня заблокировал — пропускаем
            if (blockedMeUsers.contains(postOwnerId)) {
                continue;
            }
            PostResponseDTO dto = new PostResponseDTO();
            dto.setId(post.getId());
            dto.setContent(post.getContent());
            dto.setImageUrl(post.getImageUrl());
            postResponseDTOS.add(dto);
        }
        return postResponseDTOS;
    }

    public List<PostResponseDTO> getPostYourNotBlocker(Long id) {
        List<PostResponseDTO> postResponseDTOS = new ArrayList<>();
        List<Long>blockerIUsers = blockRepository.findBlockedIdsByBlockerId(id);
        List<Posts> posts = postRepository.findAll();
        for (Posts post : posts) {
            Long postOwnerId = post.getUser().getId();
            // если ты заблокировал — пропускаем
            if (blockerIUsers.contains(postOwnerId)) {
                continue;
            }
            PostResponseDTO dto = new PostResponseDTO();
            dto.setId(post.getId());
            dto.setContent(post.getContent());
            dto.setImageUrl(post.getImageUrl());
            postResponseDTOS.add(dto);
        }
        return postResponseDTOS;
    }


    public List<PostResponseDTO> getPostYourAndINotBlocked(Long userId) {

        // Кто меня заблокировал
        List<Long> blockedMeUsers = blockRepository.findBlockerIdsByBlockedId(userId);

        // Кого я заблокировал
        List<Long> iBlockedUsers = blockRepository.findBlockedIdsByBlockerId(userId);

        List<PostResponseDTO> result = new ArrayList<>();

        for (Posts post : postRepository.findAll()) {

            Long postOwnerId = post.getUser().getId();

            // если владелец поста меня заблокировал → пропускаем
            if (blockedMeUsers.contains(postOwnerId)) continue;

            // если я заблокировал владельца поста → пропускаем
            if (iBlockedUsers.contains(postOwnerId)) continue;

            PostResponseDTO dto = new PostResponseDTO();
            dto.setId(post.getId());
            dto.setContent(post.getContent());
            dto.setImageUrl(post.getImageUrl());

            result.add(dto);
        }

        return result;
    }



    public List<PostResponseDTO> getPostsByUser(Long userId) {
        List<Posts> posts = postRepository.findByUserId(userId);

        return posts.stream().map(post -> {
            PostResponseDTO dto = new PostResponseDTO();
            dto.setContent(post.getContent());
            dto.setImageUrl(post.getImageUrl());
            return dto;
        }).toList();
    }

    public PostResponseDTO getOnlyYourPostsWithId(Long postId, Long userId) {
        Posts posts = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!posts.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can access only your posts");
        }
        PostResponseDTO dto = new PostResponseDTO();
        dto.setContent(posts.getContent());
        dto.setImageUrl(posts.getImageUrl());
        return dto;
    }

    public PostResponseDTO createPost(Long id, PostDTO postDTO) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new RuntimeException("User not found"));
        Posts post = new Posts();
        post.setContent(postDTO.getContent());
        post.setImageUrl(postDTO.getImageUrl());
        post.setUser(user);
        postRepository.save(post);
        Posts savedPost = postRepository.save(post);


        PostResponseDTO response = new PostResponseDTO();
        response.setContent(savedPost.getContent());
        response.setImageUrl(savedPost.getImageUrl());
        return response;

    }

    public String deletePost(Long id, Long currentUserId) {
        Posts post = postRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Post not found"));
        if (!post.getUser().getId().equals(currentUserId)) {
            throw new RuntimeException("You can delete only your own posts");
        }
        postRepository.delete(post);
        return "Post has been deleted";
    }

    public PostResponseDTO updatePost(Long id, PostDTO postDTO, Long currentUserId) {
        Posts post = postRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Post not found"));
        if (!post.getUser().getId().equals(currentUserId)) {
            throw new RuntimeException("You can update only your own posts");
        }
        post.setContent(postDTO.getContent());
        post.setImageUrl(postDTO.getImageUrl());
        Posts savedPost = postRepository.save(post);
        PostResponseDTO response = new PostResponseDTO();
        response.setContent(savedPost.getContent());
        response.setImageUrl(savedPost.getImageUrl());
        return response;
    }


}
