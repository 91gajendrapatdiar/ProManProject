package com.proman.TaskAllocation.controller;

import com.proman.TaskAllocation.model.Chat;
import com.proman.TaskAllocation.model.Invitation;
import com.proman.TaskAllocation.model.Project;
import com.proman.TaskAllocation.model.User;
import com.proman.TaskAllocation.repository.InviteRequest;
import com.proman.TaskAllocation.response.MessageResponse;
import com.proman.TaskAllocation.services.InvitationService;
import com.proman.TaskAllocation.services.ProjectService;
import com.proman.TaskAllocation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private InvitationService invitationService;

    // Retrieve projects with optional filters (category, tag)
    @GetMapping
    public ResponseEntity<List<Project>> getProject(@RequestParam(required = false) String category,
                                                    @RequestParam(required = false) String tag,
                                                    @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        List<Project> projects = projectService.getProjectByTeam(user, category, tag);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    // Retrieve project by ID
    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long projectId,
                                                  @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Project project = projectService.getProjectById(projectId);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    // Create a new project
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project,
                                                 @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Project createdProject = projectService.createProject(project, user);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    // Update an existing project by ID
    @PatchMapping("/{projectId}")
    public ResponseEntity<Project> updateProject(@PathVariable Long projectId,
                                                 @RequestBody Project project,
                                                 @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Project updatedProject = projectService.updateProject(project, projectId);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<MessageResponse> deleteProject(@PathVariable Long projectId,
                                                 @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        projectService.deleteProject(projectId , user.getId());
        MessageResponse res = new MessageResponse("Project  deleted Successfully");
        return new ResponseEntity<>( res, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Project>> searchProject(@RequestParam(required = false) String keyword,
                                                    @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        List<Project> projects = projectService.searchProjects( keyword,user);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }
    @GetMapping("/{projectId}/chat")
    public ResponseEntity<Chat> getChatByProjectId(@PathVariable Long projectId,
                                                  @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
          Chat chat = projectService.getChatByProjectId(projectId);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @PostMapping("/invite")
    public ResponseEntity<MessageResponse>  inviteProject(@RequestBody Project project,
                                                 @RequestBody InviteRequest req,
                                                 @RequestHeader("Authorization") String jwt)
            throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        invitationService.sendInvitaion(req.getEmail(), req.getPorjectId());
        MessageResponse res = new MessageResponse("User Invitation sent");

        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @GetMapping("/accept_invitation")
    public ResponseEntity<Invitation>  acceptInviteProject(@RequestBody Project project,
                                                          @RequestParam String token,
                                                          @RequestHeader("Authorization") String jwt)
            throws Exception {
        User user = userService.findUserProfileByJwt(jwt);
        Invitation invitation = invitationService.acceptInvitation(token , user.getId());
       projectService.addUserToProject(invitation.getProjectId(), user.getId());
//        MessageResponse res = new MessageResponse("User Invitation sent");

        return new ResponseEntity<>(invitation, HttpStatus.ACCEPTED);
    }


}
