package com.proman.TaskAllocation.services;

import com.proman.TaskAllocation.model.Chat;
import com.proman.TaskAllocation.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements  ChatService{
    @Autowired
    private ChatRepository chatRepository;
    @Override
    public Chat createChat(Chat chat) {
      return chatRepository.save(chat);

    }
}
