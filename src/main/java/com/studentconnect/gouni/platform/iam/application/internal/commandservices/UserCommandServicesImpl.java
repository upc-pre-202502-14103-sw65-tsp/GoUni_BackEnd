package com.studentconnect.gouni.platform.iam.application.internal.commandservices;

import com.studentconnect.gouni.platform.iam.application.internal.outboundservices.hashing.HashingService;
import com.studentconnect.gouni.platform.iam.application.internal.outboundservices.tokens.TokenService;
import com.studentconnect.gouni.platform.iam.domain.model.aggregates.DriverUser;
import com.studentconnect.gouni.platform.iam.domain.model.aggregates.PassengerUser;
import com.studentconnect.gouni.platform.iam.domain.model.aggregates.User;
import com.studentconnect.gouni.platform.iam.domain.model.commands.SignInCommand;
import com.studentconnect.gouni.platform.iam.domain.model.commands.SignUpCommand;
import com.studentconnect.gouni.platform.iam.domain.model.events.UserCreationEvent;
import com.studentconnect.gouni.platform.iam.domain.model.valueobjects.Roles;
import com.studentconnect.gouni.platform.iam.domain.services.UserCommandService;
import com.studentconnect.gouni.platform.iam.infrastructure.persistance.jpa.repositories.RoleRepository;
import com.studentconnect.gouni.platform.iam.infrastructure.persistance.jpa.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserCommandServicesImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final RoleRepository roleRepository;
    private final ApplicationEventPublisher eventPublisher;
    private static final Logger logger = LoggerFactory.getLogger(UserCommandServicesImpl.class);

    @Override
    public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
        var user = userRepository.findByEmail(command.email());
        if (user.isEmpty())
            throw new RuntimeException("Email not found");
        if (!hashingService.matches(command.password(), user.get().getPassword()))
            throw new RuntimeException("Invalid password");

        var token = tokenService.generateToken(user.get().getUsername());
        return Optional.of(ImmutablePair.of(user.get(), token));
    }

    @Override
    public Optional<User> handle(SignUpCommand command) {
        if (userRepository.existsByEmail(command.email()))
            throw new RuntimeException("Email already exists");
        var roles = command.roles().stream()
                .map(role -> roleRepository.findByName(role.getName())
                        .orElseThrow(() -> new RuntimeException("Role name not found")))
                .toList();

        User user;

        if (roles.stream().anyMatch(r -> r.getName() == Roles.DRIVER_ROLE)) {
            user = new DriverUser(
                    command.email(),
                    hashingService.encode(command.password()),
                    command.firstName(),
                    command.lastName(),
                    command.phoneNumber(),
                    command.profilePhotoUrl(),
                    command.dniNumber(),
                    command.licenseNumber(),
                    command.driverDescription(),
                    roles
            );
        } else if (roles.stream().anyMatch(r -> r.getName() == Roles.PASSENGER_ROLE)) {
            user = new PassengerUser(
                    command.email(),
                    hashingService.encode(command.password()),
                    command.firstName(),
                    command.lastName(),
                    command.phoneNumber(),
                    command.profilePhotoUrl(),
                    command.dniNumber(),
                    roles
            );
        } else {
            throw new RuntimeException("Invalid role for user");
        }

        var savedUser = userRepository.save(user);
        eventPublisher.publishEvent(new UserCreationEvent(savedUser, savedUser.getId()));
        return userRepository.findByEmail(command.email());
    }
}
