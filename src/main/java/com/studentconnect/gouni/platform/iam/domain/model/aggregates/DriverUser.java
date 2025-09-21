package com.studentconnect.gouni.platform.iam.domain.model.aggregates;

import com.studentconnect.gouni.platform.iam.domain.model.entities.Role;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class DriverUser extends User {
    public DriverUser() {}

    public DriverUser(
            String email,
            String password,
            String firstName,
            String lastName,
            String phoneNumber,
            String profilePhotoUrl,
            String dniNumber,
            String licenseNumber,
            String driverDescription) {
        super(email, password, firstName, lastName, phoneNumber, profilePhotoUrl, dniNumber, licenseNumber, driverDescription);
    }

    public DriverUser(
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
        super(email, password, firstName, lastName, phoneNumber, profilePhotoUrl, dniNumber, licenseNumber, driverDescription, roles);
    }
}
