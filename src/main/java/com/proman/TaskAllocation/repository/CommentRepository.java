package com.proman.TaskAllocation.repository;

import com.proman.TaskAllocation.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment , Long> {

    List<Comment> findByIssueId(Long issueId);
}
