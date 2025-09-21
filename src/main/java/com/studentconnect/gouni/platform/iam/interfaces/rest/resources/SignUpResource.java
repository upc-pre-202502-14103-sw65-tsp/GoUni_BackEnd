package com.studentconnect.gouni.platform.iam.interfaces.rest.resources;

import java.util.List;

public record SignUpResource(
        String email,
        String password,
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

