package com.studentconnect.gouni.platform.iam.interfaces.rest.transform;

import com.studentconnect.gouni.platform.iam.domain.model.aggregates.User;
import com.studentconnect.gouni.platform.iam.domain.model.entities.Role;
import com.studentconnect.gouni.platform.iam.interfaces.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {
    public static UserResource toResourceFromEntity(User user) {
        var roles = user.getRoles().stream()
                .map(Role::getStringName)
                .toList();

        return new UserResource(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getProfilePhotoUrl(),
                user.getDniNumber(),
                user.getLicenseNumber(),
                user.getDriverDescription(),
                roles
        );
    }
}
