package com.proman.TaskAllocation.services;

import com.proman.TaskAllocation.model.Comment;
import com.proman.TaskAllocation.model.Issue;
import com.proman.TaskAllocation.model.User;
import com.proman.TaskAllocation.repository.CommentRepository;
import com.proman.TaskAllocation.repository.IssueRepository;
import com.proman.TaskAllocation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements  CommentService{


    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private UserRepository userRepository;


//    @Autowired
//    public  CommentServiceImpl(CommentRepository commentRepository, IssueRepository issueRepository, UserRepository userRepository){
//        this.commentRepository = commentRepository;
//        this.issueRepository =issueRepository;
//        this.userRepository = userRepository;
//    }
    @Override
    public Comment createComment(Long issueId, Long userId, String content) throws Exception {
        Optional<Issue>  issueOptional = issueRepository.findById(issueId);
        Optional<User>  userOptional = userRepository.findById(userId);

        if(issueOptional.isEmpty()){
            throw  new Exception("issue not found with id " +issueId);
        }

        if(userOptional.isEmpty()){
            throw new Exception("user not found  with id"+userId);

        }

        Issue  issue = issueOptional.get();
        User user = userOptional.get();

        Comment comment = new Comment();

        comment.setIssue(issue);
        comment.setUser(user);
        comment.setCreatedDateTime(LocalDateTime.now());
        comment.setContent(content);

        Comment savedCommment = commentRepository.save(comment);

       issue.getComments().add(savedCommment);

        return  savedCommment;

    }

    @Override
    public void deletComment(Long commentId, Long userId) throws Exception {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        Optional<User>  userOptional = userRepository.findById(userId);

        if(commentOptional.isEmpty()){
            throw  new Exception(" Comment not found with id " +commentId);
        }

        if(userOptional.isEmpty()){
            throw new Exception("user not found  with id"+userId);

        }

        Comment comment = commentOptional.get();
        User user = userOptional.get();

        if(comment.getUser().equals(user)){
            commentRepository.delete(comment);
        }else {
            throw  new Exception("User does not have permission to delete this comment!");
        }
    }

    @Override
    public List<Comment> findCommentByIssuesId(Long issueid) {
        return commentRepository.findByIssueId(issueid);
    }
}
