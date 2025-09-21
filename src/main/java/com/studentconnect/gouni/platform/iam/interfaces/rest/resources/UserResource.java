package com.studentconnect.gouni.platform.iam.interfaces.rest.resources;

import java.util.List;
import java.util.UUID;

public record UserResource(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String phoneNumber,
        String profilePhotoUrl,
        String dniNumber,
        String licenseNumber,
        String driverDescription,
        List<String> roles
) {
}
