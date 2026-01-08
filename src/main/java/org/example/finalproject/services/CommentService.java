package org.example.finalproject.services;

import org.example.finalproject.dto.CommentDTO;
import org.example.finalproject.dto.CommentResponseDTO;
import org.example.finalproject.dto.UpdateCommentDTO;
import org.example.finalproject.entity.Comment;
import org.example.finalproject.entity.Posts;
import org.example.finalproject.entity.User;
import org.example.finalproject.repository.BlockRepository;
import org.example.finalproject.repository.CommentRepository;
import org.example.finalproject.repository.PostRepository;
import org.example.finalproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlockRepository blockRepository;

    public CommentResponseDTO createComment(CommentDTO commentDto, Long userId) {
        Posts post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setCommentPost(post);
        comment.setAuthorUser(user);
        commentRepository.save(comment);

        CommentResponseDTO commentResDTO = new CommentResponseDTO();
        commentResDTO.setContent(comment.getContent());
        commentResDTO.setPostId(post.getId());
        commentResDTO.setAuthorId(user.getId());
        commentResDTO.setAuthorUsername(user.getUsername());

        return commentResDTO;
    }

    public CommentResponseDTO createCommentNotBlocking(CommentDTO commentDto, Long userId){
        Posts post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Long> blockedMeUsers = blockRepository.findBlockerIdsByBlockedId(userId);
        List<Long> IBlockedUsers = blockRepository.findBlockedIdsByBlockerId(userId);
        Long postOwnerId = post.getUser().getId();

        CommentResponseDTO commentResDTOList = new CommentResponseDTO();
        if (blockedMeUsers.contains(postOwnerId)) {
            throw new RuntimeException("You can't comment on this post");
        }
        if (IBlockedUsers.contains(postOwnerId)) {
            throw new RuntimeException("You can't comment on this post");
        }
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setCommentPost(post);
        comment.setAuthorUser(user);
        commentRepository.save(comment);

        commentResDTOList.setContent(comment.getContent());
        commentResDTOList.setPostId(post.getId());
        commentResDTOList.setAuthorId(user.getId());
        commentResDTOList.setAuthorUsername(user.getUsername());
        return commentResDTOList;
    }

    public String deleteComment(Long commentId, Long userId) {
//        User user = userRepository.findById(id).orElseThrow(() ->
//                new RuntimeException("User not found"));
        Comment deleteComment = commentRepository.findByIdAndAuthorUserId(commentId, userId).orElseThrow(() ->
                new RuntimeException("Comment not found or access denied"));
        commentRepository.delete(deleteComment);
        return "Your comment has been deleted";

    }

    public CommentResponseDTO updateComment(UpdateCommentDTO dto, Long commentId, Long userId) {
        Comment updateComment = commentRepository.findByIdAndAuthorUserId(commentId, userId).orElseThrow(() ->
                new RuntimeException("Comment not found or access denied"));
        updateComment.setContent(dto.getContent());
        commentRepository.save(updateComment);
        CommentResponseDTO commentResDTO = new CommentResponseDTO();
        commentResDTO.setContent(updateComment.getContent());
        return commentResDTO;

    }

    public List<CommentResponseDTO> getAllComments() {
        List<Comment> lists = commentRepository.findAll();
        List<CommentResponseDTO> commentResDTOList = new ArrayList<>();
        for (Comment comment : lists) {
            CommentResponseDTO commentResDTO = new CommentResponseDTO();
            commentResDTO.setPostId(comment.getCommentPost().getId());
            commentResDTO.setId(comment.getId());
            commentResDTO.setContent(comment.getContent());
            commentResDTO.setAuthorUsername(comment.getAuthorUser().getUsername());
            commentResDTO.setAuthorId(comment.getAuthorUser().getId());
            commentResDTO.setDate(comment.getDate());
            commentResDTOList.add(commentResDTO);
        }
        // CommentResponseDTO commentResDTO = new CommentResponseDTO();

        return commentResDTOList;
    }

    public CommentResponseDTO getCommentWithId(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new RuntimeException("Comment not found"));
        CommentResponseDTO commentResDTO = new CommentResponseDTO();
        commentResDTO.setContent(comment.getContent());
        commentResDTO.setAuthorUsername(comment.getAuthorUser().getUsername());
        commentResDTO.setId(comment.getId());
        commentResDTO.setAuthorId(comment.getAuthorUser().getId());
        commentResDTO.setDate(comment.getDate());
        return commentResDTO;
    }

    public List<CommentResponseDTO> getAllCommentsWhoNotBlockedYou(Long userId) {
        List<Long> blockedMeUsers = blockRepository.findBlockerIdsByBlockedId(userId);
        List<CommentResponseDTO> commentResDTOList = new ArrayList<>();
        List<Comment> comments = commentRepository.findAll();
        for (Comment comment : comments) {
            Long commentOwnerId = comment.getAuthorUser().getId();
            if (!blockedMeUsers.contains(commentOwnerId)) {
                CommentResponseDTO commentResDTO = new CommentResponseDTO();
                commentResDTO.setPostId(comment.getCommentPost().getId());
                commentResDTO.setId(comment.getId());
                commentResDTO.setContent(comment.getContent());
                commentResDTO.setAuthorUsername(comment.getAuthorUser().getUsername());
                commentResDTO.setAuthorId(comment.getAuthorUser().getId());
                commentResDTO.setDate(comment.getDate());
                commentResDTOList.add(commentResDTO);
            }

        }
        return commentResDTOList;
    }

    public List<CommentResponseDTO> getAllCommentsWhoNotBlockerYou(Long userId) {
        List<Long> IBlockedUsers = blockRepository.findBlockedIdsByBlockerId(userId);
        List<CommentResponseDTO> commentResDTOList = new ArrayList<>();
        List<Comment> comments = commentRepository.findAll();
        for (Comment comment : comments) {
            Long commentOwnerId = comment.getAuthorUser().getId();
            if (!IBlockedUsers.contains(commentOwnerId)) {
                CommentResponseDTO commentResDTO = new CommentResponseDTO();
                commentResDTO.setPostId(comment.getCommentPost().getId());
                commentResDTO.setId(comment.getId());
                commentResDTO.setContent(comment.getContent());
                commentResDTO.setAuthorUsername(comment.getAuthorUser().getUsername());
                commentResDTO.setAuthorId(comment.getAuthorUser().getId());
                commentResDTO.setDate(comment.getDate());
                commentResDTOList.add(commentResDTO);
            }
        }
        return commentResDTOList;
    }

    public List<CommentResponseDTO> getAllCommentsWhoAndYouNotBlocked(Long userId) {
        List<Long> blockedMeUsers = blockRepository.findBlockerIdsByBlockedId(userId);
        List<Long> IBlockedUsers = blockRepository.findBlockedIdsByBlockerId(userId);
        List<CommentResponseDTO> commentResDTOList = new ArrayList<>();
        List<Comment> comments = commentRepository.findAll();
        for (Comment comment : comments) {
            Long commentId = comment.getAuthorUser().getId();
            if (IBlockedUsers.contains(commentId)) {continue;}
            if (blockedMeUsers.contains(commentId)) {continue;}
            CommentResponseDTO commentResDTO = new CommentResponseDTO();
            commentResDTO.setPostId(comment.getCommentPost().getId());
            commentResDTO.setId(comment.getId());
            commentResDTO.setContent(comment.getContent());
            commentResDTO.setAuthorUsername(comment.getAuthorUser().getUsername());
            commentResDTO.setAuthorId(comment.getAuthorUser().getId());
            commentResDTO.setDate(comment.getDate());
            commentResDTOList.add(commentResDTO);
        }
        return commentResDTOList;
    }
}
