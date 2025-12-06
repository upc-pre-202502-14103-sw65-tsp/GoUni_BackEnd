package com.studentconnect.gouni.platform.iam.infrastructure.persistance.jpa.repositories;

import com.studentconnect.gouni.platform.iam.domain.model.aggregates.DriverUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DriverUserRepository extends JpaRepository<DriverUser, UUID> {
    Optional<DriverUser> findByEmail(String email);
}
