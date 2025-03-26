package com.proman.TaskAllocation.repository;

import com.proman.TaskAllocation.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository  extends JpaRepository<Chat,Long> {
}
