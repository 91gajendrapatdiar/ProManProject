package  com.proman.TaskAllocation.controller;
import com.proman.TaskAllocation.model.Comment;
import com.proman.TaskAllocation.model.User;
import com.proman.TaskAllocation.request.CreateCommentRequest;
import com.proman.TaskAllocation.response.MessageResponse;
import com.proman.TaskAllocation.services.CommentService;
import com.proman.TaskAllocation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {



    @Autowired
    private CommentService commentService;


    @Autowired
    private UserService userService;
    @PostMapping
    public ResponseEntity<Comment> createComment(
            @RequestBody CreateCommentRequest req ,
            @RequestHeader("Authorization") String jwt) throws Exception{
        User user = userService.findUserProfileByJwt(jwt);
        Comment createComment = commentService.createComment(req.getIssueId(),user.getId(),  req.getContent());
        return  new ResponseEntity<>(createComment, HttpStatus.CREATED);
    }

    @DeleteMapping("/{commentId}")
    public  ResponseEntity<MessageResponse> deletComment(@PathVariable Long commentId,
                                                         @RequestHeader("Authorization") String jwt)
            throws Exception{
        User user = userService.findUserProfileByJwt(jwt);
        commentService.deletComment(commentId,user.getId());
        MessageResponse res =  new MessageResponse();
        res.setMessage("comment deleted successfully");
        return  new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{issueId}")
    public  ResponseEntity<List<Comment>> getCommentsByIssueId(@PathVariable Long issueId){
        List<Comment> comments = commentService.findCommentByIssuesId(issueId);
        return new ResponseEntity<>(comments, HttpStatus.OK);

    }



}
