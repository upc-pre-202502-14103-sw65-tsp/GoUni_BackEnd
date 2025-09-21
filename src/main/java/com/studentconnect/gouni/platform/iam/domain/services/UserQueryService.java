package com.studentconnect.gouni.platform.iam.domain.services;

import com.studentconnect.gouni.platform.iam.domain.model.aggregates.DriverUser;
import com.studentconnect.gouni.platform.iam.domain.model.aggregates.PassengerUser;
import com.studentconnect.gouni.platform.iam.domain.model.aggregates.User;
import com.studentconnect.gouni.platform.iam.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface UserQueryService {
    Optional<User> handle(GetUserByIdQuery query);
    Optional<User> handle(GetUserByEmailQuery query);

    List<User> handle(GetAllUsersQuery query);
    List<User> handle(GetAllUsersByRoleQuery query);
}
