package com.proman.TaskAllocation.services;

import com.proman.TaskAllocation.model.Message;

import java.util.List;

public interface MessageService {

    Message sendMessage(Long senderId , Long projectId, String  content) throws  Exception;

    List<Message> getMessagesByProjectId(Long projectId) throws  Exception;
}
