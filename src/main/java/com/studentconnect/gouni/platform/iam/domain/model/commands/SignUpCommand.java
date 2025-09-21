package com.studentconnect.gouni.platform.iam.domain.model.commands;

import com.studentconnect.gouni.platform.iam.domain.model.entities.Role;

import java.util.List;

public record SignUpCommand(
        String email,
        String password,
        String firstName,
        String lastName,
        String phoneNumber,
        String profilePhotoUrl,
        String dniNumber,
        String licenseNumber,
        String driverDescription,
        List<Role> roles) {
}
