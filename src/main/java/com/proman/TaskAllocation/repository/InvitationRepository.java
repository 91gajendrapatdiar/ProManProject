package com.proman.TaskAllocation.repository;

import com.proman.TaskAllocation.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation , Long> {

    Invitation findByToken(String token);

    Invitation findByEmail(String userEmail);
}
