package com.anonconfess.backend.service;

import com.anonconfess.backend.dto.ConfessionResponse;
import com.anonconfess.backend.dto.CreateCommentRequest;

public interface CommentService {
    ConfessionResponse addComment(Long confessionId, CreateCommentRequest request);
}