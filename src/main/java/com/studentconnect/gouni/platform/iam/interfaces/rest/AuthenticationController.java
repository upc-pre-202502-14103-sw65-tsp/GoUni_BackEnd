package com.studentconnect.gouni.platform.iam.interfaces.rest;

import com.studentconnect.gouni.platform.iam.domain.services.UserCommandService;
import com.studentconnect.gouni.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.studentconnect.gouni.platform.iam.interfaces.rest.resources.SignInResource;
import com.studentconnect.gouni.platform.iam.interfaces.rest.resources.SignUpResource;
import com.studentconnect.gouni.platform.iam.interfaces.rest.resources.UserResource;
import com.studentconnect.gouni.platform.iam.interfaces.rest.transform.AuthenticatedUserResourceFromEntityAssembler;
import com.studentconnect.gouni.platform.iam.interfaces.rest.transform.SignInCommandFromResourceAssembler;
import com.studentconnect.gouni.platform.iam.interfaces.rest.transform.SignUpCommandFromResourceAssembler;
import com.studentconnect.gouni.platform.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE })
@RestController
@RequestMapping(value = "/api/v1/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Authentication Endpoints")
public class AuthenticationController {
    private final UserCommandService userCommandService;

    /**
     * Handles the sign-in request.
     * @param signInResource the sign-in request body.
     * @return the authenticated user resource.
     */
    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticatedUserResource> signIn(@RequestBody SignInResource signInResource) {
        var signInCommand = SignInCommandFromResourceAssembler.toCommandFromResource(signInResource);

        var authenticatedUser = userCommandService.handle(signInCommand);

        if (authenticatedUser.isEmpty()) { return ResponseEntity.notFound().build(); }

        var authenticatedUserResource = AuthenticatedUserResourceFromEntityAssembler
            .toResourceFromEntity(
                authenticatedUser.get().getLeft(), authenticatedUser.get().getRight());

        return ResponseEntity.ok(authenticatedUserResource);
    }

    /**
     * Handles the sign-up request.
     * @param signUpResource the sign-up request body.
     * @return the created user resource.
     */
    @PostMapping("/sign-up")
    public ResponseEntity<UserResource> signUp(@RequestBody SignUpResource signUpResource) {
        var signUpCommand = SignUpCommandFromResourceAssembler.toCommandFromResource(signUpResource);
        var user = userCommandService.handle(signUpCommand);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return new ResponseEntity<>(userResource, HttpStatus.CREATED);
    }
}
