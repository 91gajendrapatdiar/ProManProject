package com.proman.TaskAllocation.services;

import com.proman.TaskAllocation.model.Issue;
import com.proman.TaskAllocation.model.User;
import com.proman.TaskAllocation.request.IssueRequest;
import jdk.jshell.spi.ExecutionControl;

import java.util.List;
import java.util.Optional;

public interface IssueService {

    Issue getIssueById(Long issuedId) throws  Exception;

    List<Issue>  getIssueByProjectId(Long projectId) throws  Exception;

    Issue  createIssue(IssueRequest issueRequest, User user) throws Exception;

//    Optional<Issue>  updateIssue(Long issueid, IssueRequest updatedIssue , Long userid) throws  Exception;
//
    void deleteIssue(Long issueId, Long userid) throws  Exception;

//    List<Issue> getIssuesByAssigneeId(Long assigneeId) throws  Exception;
//
//    List<Issue>  searchIssues(String title, String status, String priority , Long assigneeId) throws  Exception;
//

    Issue addUserToIssue(Long  issueId, Long userId) throws  Exception;

    Issue updateStatus(Long issueId, String status) throws  Exception;

}
