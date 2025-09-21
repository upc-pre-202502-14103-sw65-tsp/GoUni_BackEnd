package com.studentconnect.gouni.platform.iam.interfaces.rest.resources;

public record CreateUserResource(
        String email,
        String passwordHash
) {
}
