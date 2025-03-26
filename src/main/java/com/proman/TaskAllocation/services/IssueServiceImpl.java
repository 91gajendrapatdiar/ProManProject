package com.proman.TaskAllocation.services;

import com.proman.TaskAllocation.model.Issue;
import com.proman.TaskAllocation.model.Project;
import com.proman.TaskAllocation.model.User;
import com.proman.TaskAllocation.repository.IssueRepository;
import com.proman.TaskAllocation.request.IssueRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IssueServiceImpl implements  IssueService{

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private  ProjectService projectService;

    @Autowired
    private UserService userService;
    @Override
    public Issue getIssueById(Long issuedId) throws Exception {
      Optional<Issue> issue = issueRepository.findById(issuedId);
      if(issuedId.describeConstable().isPresent()){
          return issue.get();
      }
      throw new Exception("No issue found with issueid" +issuedId);
    }

    @Override
    public List<Issue> getIssueByProjectId(Long projectId) throws Exception {
        return  issueRepository.findByProjectId(projectId);
    }

    @Override
    public Issue createIssue(IssueRequest issueRequest, User user) throws Exception {
        Project project = projectService.getProjectById(issueRequest.getProjectId());
        Issue issue = new Issue();
        issue.setTitle(issueRequest.getTitle());
        issue.setDescription(issueRequest.getDescription());
        issue.setStatus(issueRequest.getStatus());
        issue.setProjectID(issue.getProjectID());
        issue.setPriority(issue.getPriority());
        issue.setDueDate(issueRequest.getDueDate());


        issue.setProject(project);

        return issueRepository.save(issue);
    }


    @Override
    public void deleteIssue(Long issueId, Long userid) throws Exception {
        getIssueById(issueId);

          issueRepository.deleteById(issueId);
    }

    @Override
    public Issue addUserToIssue(Long issueId, Long userId) throws Exception {
        User user = userService.findUserById(userId);
        Issue issue = getIssueById(issueId);

        issue.setAssignee(user);
        return issueRepository.save(issue);

    }

    @Override
    public Issue updateStatus(Long issueId, String status) throws Exception {
        Issue issue = getIssueById(issueId);
        issue.setStatus(status);
        return issueRepository.save(issue);
    }
}
