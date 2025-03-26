package com.proman.TaskAllocation.controller;

import com.proman.TaskAllocation.model.Chat;
import com.proman.TaskAllocation.model.Message;
import com.proman.TaskAllocation.model.User;
import com.proman.TaskAllocation.request.CreateMessageRequest;
import com.proman.TaskAllocation.services.MessageService;
import com.proman.TaskAllocation.services.ProjectService;
import com.proman.TaskAllocation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;


    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody CreateMessageRequest request) throws Exception {
        // Check if the senderId is not null before calling the service
        if (request.getSenderId() == null) {
            throw new IllegalArgumentException("Sender ID cannot be null");
        }

        // Find the user by senderId, handle potential nulls with proper exceptions
        User user = userService.findUserById(request.getSenderId());
        if (user == null) {
            throw new Exception("User not found with ID: " + request.getSenderId());
        }

        // Ensure that the project has a chat, otherwise, throw an exception
        Chat chat = projectService.getProjectById(request.getProjectId()).getChat();
        if (chat == null) {
            throw new Exception("Chat not found for project ID: " + request.getProjectId());
        }

        // Send the message using the service and return the response entity
        Message sentMessage = messageService.sendMessage(
                request.getSenderId(),
                request.getProjectId(),
                request.getContent()
        );

        return ResponseEntity.ok(sentMessage);
    }

    @GetMapping("/chat/{projectId}")
    public ResponseEntity<List<Message>> getMessagesByChatId(@PathVariable Long projectId) throws Exception {
        // Fetch messages by projectId, which represents the chat ID
        List<Message> messages = messageService.getMessagesByProjectId(projectId);
        return ResponseEntity.ok(messages);
    }
}
