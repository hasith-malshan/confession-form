package com.hasithmalshan.confession_form.service;

import com.hasithmalshan.confession_form.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(CommentDTO commentDTO);
    CommentDTO getCommentById(Long id);
    List<CommentDTO> getAllComments();
    List<CommentDTO> getCommentsByPostId(Long postId);
    List<CommentDTO> getCommentsByUserId(Long userId);
    CommentDTO updateComment(Long id, CommentDTO commentDTO);
    void deleteComment(Long id);
    boolean commentExists(Long id);
}
