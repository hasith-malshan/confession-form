package com.hasithmalshan.confession_form.service.impl;

import com.hasithmalshan.confession_form.dto.CommentDTO;
import com.hasithmalshan.confession_form.exception.ResourceNotFoundException;
import com.hasithmalshan.confession_form.model.Comment;
import com.hasithmalshan.confession_form.model.Post;
import com.hasithmalshan.confession_form.model.User;
import com.hasithmalshan.confession_form.repo.CommentRepository;
import com.hasithmalshan.confession_form.repo.PostRepository;
import com.hasithmalshan.confession_form.repo.UserRepository;
import com.hasithmalshan.confession_form.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public CommentDTO createComment(CommentDTO commentDTO) {
        User user = userRepository.findById(commentDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", commentDTO.getUserId()));
        Post post = postRepository.findById(commentDTO.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", commentDTO.getPostId()));

        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setUser(user);
        comment.setPost(post);
        
        Comment savedComment = commentRepository.save(comment);
        return convertToDTO(savedComment);
    }

    @Override
    public CommentDTO getCommentById(Long id) {
        Comment foundComment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));
        return convertToDTO(foundComment);
    }

    @Override
    public List<CommentDTO> getAllComments() {
        return commentRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<CommentDTO> getCommentsByUserId(Long userId) {
        return commentRepository.findByUserId(userId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public CommentDTO updateComment(Long id, CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", id));
        comment.setContent(commentDTO.getContent());
        Comment updatedComment = commentRepository.save(comment);
        return convertToDTO(updatedComment);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public boolean commentExists(Long id) {
        return commentRepository.existsById(id);
    }

    private CommentDTO convertToDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setUserId(comment.getUser().getId());
        commentDTO.setPostId(comment.getPost().getId());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setUpdatedAt(comment.getUpdatedAt());
        return commentDTO;
    }
}
