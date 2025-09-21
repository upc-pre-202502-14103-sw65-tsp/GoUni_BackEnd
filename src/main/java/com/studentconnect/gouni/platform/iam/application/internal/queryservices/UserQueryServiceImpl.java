package com.studentconnect.gouni.platform.iam.application.internal.queryservices;

import com.studentconnect.gouni.platform.iam.domain.model.aggregates.User;
import com.studentconnect.gouni.platform.iam.domain.model.queries.*;
import com.studentconnect.gouni.platform.iam.domain.services.UserQueryService;
import com.studentconnect.gouni.platform.iam.infrastructure.persistance.jpa.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;

    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findUserById(query.userId());
    }

    @Override
    public Optional<User> handle(GetUserByEmailQuery query) {
        return userRepository.findByEmail(query.email());
    }

    @Override
    public List<User> handle(GetAllUsersQuery query) {
        return userRepository.findAll();
    }

    @Override
    public List<User> handle(GetAllUsersByRoleQuery query) {
        return userRepository.findAllByRoles_Name(query.role());
    }
}
