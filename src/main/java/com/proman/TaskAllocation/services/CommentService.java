package com.proman.TaskAllocation.services;

import com.proman.TaskAllocation.model.Comment;

import java.util.List;

public interface CommentService {
    Comment createComment(Long issueId, Long userId , String content) throws  Exception;

    void deletComment(Long commentId, Long  userId) throws  Exception;

    List<Comment> findCommentByIssuesId(Long issueid);
}
