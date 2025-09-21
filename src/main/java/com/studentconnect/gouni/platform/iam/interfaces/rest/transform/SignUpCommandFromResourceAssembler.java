package com.studentconnect.gouni.platform.iam.interfaces.rest.transform;

import com.studentconnect.gouni.platform.iam.domain.model.commands.SignUpCommand;
import com.studentconnect.gouni.platform.iam.domain.model.entities.Role;
import com.studentconnect.gouni.platform.iam.interfaces.rest.resources.SignUpResource;

import java.util.ArrayList;

public class SignUpCommandFromResourceAssembler {
  public static SignUpCommand toCommandFromResource(SignUpResource resource) {
    var roles = resource.roles() != null
        ? resource.roles().stream().map(Role::toRoleFromName).toList()
        : new ArrayList<Role>();
    return new SignUpCommand(
            resource.email(),
            resource.password(),
            resource.firstName(),
            resource.lastName(),
            resource.phoneNumber(),
            resource.profilePhotoUrl(),
            resource.dniNumber(),
            resource.licenseNumber(),
            resource.driverDescription(),
            roles
    );
  }
}
