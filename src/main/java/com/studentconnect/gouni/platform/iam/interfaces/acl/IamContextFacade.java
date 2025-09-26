package com.studentconnect.gouni.platform.iam.interfaces.acl;

import com.studentconnect.gouni.platform.iam.domain.model.aggregates.PassengerUser;
import com.studentconnect.gouni.platform.iam.domain.model.aggregates.User;

import java.util.Optional;
import java.util.UUID;

public interface IamContextFacade {
    Optional<User> fetchUserById(UUID userId);
    Optional<PassengerUser> fetchPassengerUserById(UUID passengerUserId);

    UUID fetchUserIdByEmail(String email);
}
