package com.studentconnect.gouni.platform.iam.domain.model.aggregates;

import com.studentconnect.gouni.platform.iam.domain.model.entities.Role;
import com.studentconnect.gouni.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
public class User extends AuditableAbstractAggregateRoot<User> {
    @Email
    @NotNull
    @Column(unique = true)
    protected String email;

    @NotNull
    @Size(max = 120)
    protected String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    protected Set<Role> roles = new HashSet<>();

    @NotNull
    @Size(min = 1, max = 30)
    private String firstName;

    @NotNull
    @Size(min = 1, max = 30)
    private String lastName;

    @NotNull
    private String phoneNumber;

    private String profilePhotoUrl;

    @NotNull
    @Size(min = 8, max = 8)
    private String dniNumber;

    private String licenseNumber;

    private String driverDescription;

    public User() { }

    public User(
            String email,
            String password,
            String firstName,
            String lastName,
            String phoneNumber,
            String profilePhotoUrl,
            String dniNumber
    ) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.profilePhotoUrl = profilePhotoUrl;
        this.dniNumber = dniNumber;
        this.licenseNumber = null;
        this.driverDescription = null;
        this.roles = new HashSet<>();
    }

    public User(
            String email,
            String password,
            String firstName,
            String lastName,
            String phoneNumber,
            String profilePhotoUrl,
            String dniNumber,
            String licenseNumber,
            String driverDescription
    ) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.profilePhotoUrl = profilePhotoUrl;
        this.dniNumber = dniNumber;
        this.licenseNumber = licenseNumber;
        this.driverDescription = driverDescription;
        this.roles = new HashSet<>();
    }

    public User(
            String email,
            String password,
            String firstName,
            String lastName,
            String phoneNumber,
            String profilePhotoUrl,
            String dniNumber,
            List<Role> roles) {
        this(email, password, firstName, lastName, phoneNumber, profilePhotoUrl, dniNumber);
        addRoles(roles);
    }

    public User(
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
        this(email, password, firstName, lastName, phoneNumber, profilePhotoUrl, dniNumber, licenseNumber, driverDescription);
        addRoles(roles);
    }

    public User addRoles(List<Role> roles) {
        var validatedRoleSet = Role.validateRoleSet(roles);
        this.roles.addAll(validatedRoleSet);
        return this;
    }

    public String getUsername() {
        return email;
    }
}
