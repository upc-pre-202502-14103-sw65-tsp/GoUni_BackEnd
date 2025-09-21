package com.studentconnect.gouni.platform.iam.domain.model.aggregates;

import com.studentconnect.gouni.platform.iam.domain.model.entities.Role;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class PassengerUser extends User {
    public PassengerUser() {}

    public PassengerUser(
            String email,
            String password,
            String firstName,
            String lastName,
            String phoneNumber,
            String profilePhotoUrl,
            String dniNumber) {
        super(email, password, firstName, lastName, phoneNumber, profilePhotoUrl, dniNumber);
    }

    public PassengerUser(
            String email,
            String password,
            String firstName,
            String lastName,
            String phoneNumber,
            String profilePhotoUrl,
            String dniNumber,
            List<Role> roles) {
        super(email, password, firstName, lastName, phoneNumber, profilePhotoUrl, dniNumber, roles);
    }
}
