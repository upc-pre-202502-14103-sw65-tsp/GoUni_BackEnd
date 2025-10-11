package com.studentconnect.gouni.platform.iam.interfaces.rest;

import com.studentconnect.gouni.platform.iam.domain.model.queries.GetAllUsersByRoleQuery;
import com.studentconnect.gouni.platform.iam.domain.model.queries.GetAllUsersQuery;
import com.studentconnect.gouni.platform.iam.domain.model.valueobjects.Roles;
import com.studentconnect.gouni.platform.iam.domain.services.UserQueryService;
import com.studentconnect.gouni.platform.iam.interfaces.rest.resources.UserResource;
import com.studentconnect.gouni.platform.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE })
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "Users Endpoints")
public class UsersController {
    private final UserQueryService userQueryService;

    /**
     * Fetches all users.
     * @return a list of all user resources.
     */
    @GetMapping
    public ResponseEntity<List<UserResource>> getAllUsers() {
        var users = userQueryService.handle(new GetAllUsersQuery());
        List<UserResource> resources = users.stream()
                .map(UserResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    /**
     * Fetches all drivers.
     * @return a list of all driver resources.
     */
    @GetMapping("/drivers")
    public ResponseEntity<List<UserResource>> getAllDrivers() {
        var drivers = userQueryService.handle(new GetAllUsersByRoleQuery(Roles.DRIVER_ROLE));
        List<UserResource> resources = drivers.stream()
                .map(UserResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }
}
