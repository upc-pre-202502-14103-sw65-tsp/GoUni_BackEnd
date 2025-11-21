package com.studentconnect.gouni.platform.carpooling.interfaces.rest;

import com.studentconnect.gouni.platform.carpooling.domain.model.queries.*;
import com.studentconnect.gouni.platform.carpooling.domain.model.valueobjects.RideStatus;
import com.studentconnect.gouni.platform.carpooling.domain.services.RideCommandService;
import com.studentconnect.gouni.platform.carpooling.domain.services.RideQueryService;
import com.studentconnect.gouni.platform.carpooling.interfaces.rest.resources.CreateRideResource;
import com.studentconnect.gouni.platform.carpooling.interfaces.rest.resources.RideResource;
import com.studentconnect.gouni.platform.carpooling.interfaces.rest.transform.CreateRideCommandFromResourceAssembler;
import com.studentconnect.gouni.platform.carpooling.interfaces.rest.transform.RideResourceFromEntityAssembler;
import com.studentconnect.gouni.platform.notifications.application.internal.outboundservices.acl.ExternalIamService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE })
@RestController
@RequestMapping(value = "/api/v1/rides", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Rides", description = "Rides Management Endpoint")
public class RidesController {
    private final RideCommandService commandService;
    private final RideQueryService queryService;
    private final ExternalIamService externalIamService;

    @PostMapping
    public ResponseEntity<RideResource> createRide(@RequestBody CreateRideResource resource) {
        var createRideCommand = CreateRideCommandFromResourceAssembler.toCommandFromResource(resource);
        var rideResult = commandService.handle(createRideCommand);
        if (rideResult.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var rideResource = RideResourceFromEntityAssembler.toResourceFromEntity(rideResult.get());
        return new ResponseEntity<>(rideResource, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RideResource>> getAllRides() {
        var getAllRidesQuery = queryService.handle(new GetAllRidesQuery());
        if (getAllRidesQuery.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        var rideResources = getAllRidesQuery.stream()
                .map(RideResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(rideResources);
    }

    @GetMapping("/driver/{driverUserId}")
    public ResponseEntity<List<RideResource>> getAllRidesByDriverUserId(@PathVariable UUID driverUserId) {
        var userResult = externalIamService.fetchDriverUserById(driverUserId);
        if (userResult.isEmpty()) {
            throw new IllegalArgumentException("Driver User ID not found for the provided email");
        }

        var getAllRidesByDriverUserIdQuery = queryService.handle(new GetAllRidesByDriverUserIdQuery(driverUserId));
        if (getAllRidesByDriverUserIdQuery.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        var rideResources = getAllRidesByDriverUserIdQuery.stream()
                .map(RideResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(rideResources);
    }

    @GetMapping("/passenger/{passengerUserId}")
    public ResponseEntity<List<RideResource>> getAllRidesByPassengerUserId(@PathVariable UUID passengerUserId) {
        var userResult = externalIamService.fetchPassengerUserById(passengerUserId);
        if (userResult.isEmpty()) {
            throw new IllegalArgumentException("Passenger User ID not found for the provided email");
        }

        var getAllRidesByPassengerUserIdQuery = queryService.handle(new GetAllRidesByPassengerUserIdQuery(passengerUserId));
        if (getAllRidesByPassengerUserIdQuery.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        var rideResources = getAllRidesByPassengerUserIdQuery.stream()
                .map(RideResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(rideResources);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<RideResource>> getaLLRidesByStatus(@PathVariable String status) {
        var rideStatus = RideStatus.valueOf(status.toUpperCase());
        var query = new GetAllRidesByStatusQuery(rideStatus);
        var rides = queryService.handle(query);
        var resources = rides.stream()
                .map(RideResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/location")
    public ResponseEntity<List<RideResource>> getRidesByLocation(@RequestParam String location) {
        var query = new GetAllRidesByLocationQuery(location);
        var rides = queryService.handle(query);
        var resources = rides.stream()
                .map(RideResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }
}
