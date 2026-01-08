package org.example.finalproject.controllers;

import org.example.finalproject.dto.CommentDTO;
import org.example.finalproject.dto.CommentResponseDTO;
import org.example.finalproject.dto.UpdateCommentDTO;
import org.example.finalproject.entity.Comment;
import org.example.finalproject.entity.User;
import org.example.finalproject.repository.CommentRepository;
import org.example.finalproject.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/comments/")
public class CommentControllers {

    private CommentService commentService;

    public CommentControllers(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/create-comment")
    public CommentResponseDTO createComment(CommentDTO commentDto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return commentService.createComment(commentDto, user.getId());
    }

    @PostMapping("/create-comment-not-blocking")
    public CommentResponseDTO createCommentNotBlocking(CommentDTO commentDto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return commentService.createCommentNotBlocking(commentDto, user.getId());
    }

    @PutMapping("/update-your-comment/{commentId}")
    public CommentResponseDTO updateComment(UpdateCommentDTO commentDto, @PathVariable Long commentId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return commentService.updateComment(commentDto, commentId, user.getId());
    }

    @DeleteMapping("/delete-comment/{id}")
    public String deleteComment(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return commentService.deleteComment(id, user.getId());
    }

    @GetMapping("get-all-comments")
    public List<CommentResponseDTO> getAllComments(Authentication authentication) {
        return commentService.getAllComments();
    }

    @GetMapping("/get-comment-with-id/{commentId}")
    public CommentResponseDTO getCommentById(@PathVariable Long commnetId) {
        return commentService.getCommentWithId(commnetId);
    }

    @GetMapping("/get-comments-not-blocked-you")
    public List<CommentResponseDTO> getNotBlockedComments(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return commentService.getAllCommentsWhoNotBlockedYou(user.getId());
    }

    @GetMapping("/get-comments-not-blocker-you")
    public List<CommentResponseDTO> getNotBlockerComments(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return commentService.getAllCommentsWhoNotBlockerYou(user.getId());
    }

    @GetMapping("/get-comments-that-between-not-blocking")
    public List<CommentResponseDTO> getCommentsThatYouAndOthersNotBlocking(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return commentService.getAllCommentsWhoAndYouNotBlocked(user.getId());
    }
}
