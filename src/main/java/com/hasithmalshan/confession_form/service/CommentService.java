package com.hasithmalshan.confession_form.service;

import com.hasithmalshan.confession_form.dto.CommentDTO;
import com.hasithmalshan.confession_form.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Comment createComment(Comment comment);
    CommentDTO getCommentById(Long id);
    List<Comment> getAllComments();
    List<Comment> getCommentsByPostId(Long postId);
    List<Comment> getCommentsByUserId(Long userId);
    Comment updateComment(Long id, Comment comment);
    void deleteComment(Long id);
    boolean commentExists(Long id);
    CommentDTO convertToDTO(Comment comment);
}

