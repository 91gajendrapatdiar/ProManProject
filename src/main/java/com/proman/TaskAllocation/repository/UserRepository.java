package com.proman.TaskAllocation.repository;

import com.proman.TaskAllocation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
