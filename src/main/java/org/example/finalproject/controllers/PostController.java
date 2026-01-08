package org.example.finalproject.controllers;

import jakarta.validation.Valid;
//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.example.finalproject.dto.PostDTO;
import org.example.finalproject.dto.PostResponseDTO;
import org.example.finalproject.entity.Posts;
import org.example.finalproject.entity.User;
import org.example.finalproject.services.AuthService;
import org.example.finalproject.services.PostService;
import org.example.finalproject.utils.JwtUtil;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;

    private final JwtUtil jwtUtil;

    public PostController(PostService postService, JwtUtil jwtUtil) {
        this.postService = postService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("all-posts")
    public List<PostResponseDTO> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/get-all-post-with-id/{id}")
    public PostResponseDTO getPostById(@PathVariable Long id){
        return postService.getPostById(id);
    }


    @GetMapping("/getpostnotblocked/{id}")
    public List<PostResponseDTO> getPostNotBlocked(@PathVariable Long id){
        return postService.getPostYourNotBlocked(id);
    }

    @GetMapping("/getpostnotblocker/{id}")
    public List<PostResponseDTO> getPostNotBlocker(@PathVariable Long id){
        return postService.getPostYourNotBlocker(id);
    }

    @GetMapping("/getpost-your-and-i-notblocker/{id}")
    public List<PostResponseDTO> getPostYourAndINotBlocked(@PathVariable Long id){
        return postService.getPostYourAndINotBlocked(id);
    }


    @GetMapping("get-only-your-posts")
    public List<PostResponseDTO> getOnlyYourPosts(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return postService.getPostsByUser(user.getId());
    }

    @GetMapping("/get-your-post/{postId}")
    public PostResponseDTO getOnlyYourPostById(@PathVariable Long postId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return postService.getOnlyYourPostsWithId(postId, user.getId());
    }

    @PostMapping("/new-post")
    public PostResponseDTO createPost(@RequestBody PostDTO dto,
                                      @RequestHeader("Authorization") String authHeader) {

        // 1. Вытаскиваем токен из заголовка
        String token = authHeader.replace("Bearer ", "");

        // 2. Получаем userId из токена
        Long userId = jwtUtil.getUserIdFromToken(token);

        // 3. Передаём в сервис
        return postService.createPost(userId, dto);
    }


    @PostMapping("new-post-tc")
    public PostResponseDTO createPostNew(@RequestBody PostDTO dto, Authentication authentication) {
        User user = (User) authentication.getPrincipal(); // Spring уже положил User в SecurityContext
        return postService.createPost(user.getId(), dto);
    }

    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return postService.deletePost(id, user.getId());
    }

    @PutMapping("/{id}")
    public PostResponseDTO updatePost(@PathVariable Long id, @RequestBody PostDTO dto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return postService.updatePost(id, dto, user.getId());
    }

}
