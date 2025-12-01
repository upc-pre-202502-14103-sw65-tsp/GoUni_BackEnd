package com.studentconnect.gouni.platform.carpooling.interfaces.rest;

import com.studentconnect.gouni.platform.carpooling.domain.model.queries.*;
import com.studentconnect.gouni.platform.carpooling.domain.services.RatingCommandService;
import com.studentconnect.gouni.platform.carpooling.domain.services.RatingQueryService;
import com.studentconnect.gouni.platform.carpooling.interfaces.rest.resources.CreateRatingResource;
import com.studentconnect.gouni.platform.carpooling.interfaces.rest.resources.DriverRatingStatsResource;
import com.studentconnect.gouni.platform.carpooling.interfaces.rest.resources.RatingResource;
import com.studentconnect.gouni.platform.carpooling.interfaces.rest.transform.CreateRatingCommandFromResourceAssembler;
import com.studentconnect.gouni.platform.carpooling.interfaces.rest.transform.RatingResourceFromEntityAssembler;
import com.studentconnect.gouni.platform.iam.infrastructure.persistance.jpa.repositories.DriverUserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE })
@RestController
@RequestMapping(value = "/api/v1/ratings", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Ratings", description = "Rating Management Endpoints")
public class RatingsController {
    private final RatingCommandService commandService;
    private final RatingQueryService queryService;
    private final DriverUserRepository driverUserRepository;

    /**
     * Creates a new rating for a completed ride.
     * @param resource the rating creation request
     * @return the created rating resource
     */
    @PostMapping
    public ResponseEntity<?> createRating(@RequestBody CreateRatingResource resource) {
        try {
            var createRatingCommand = CreateRatingCommandFromResourceAssembler.toCommandFromResource(resource);
            var rating = commandService.handle(createRatingCommand);
            
            if (rating.isEmpty()) {
                return ResponseEntity.badRequest().body("Failed to create rating");
            }
            
            var ratingResource = RatingResourceFromEntityAssembler.toResourceFromEntity(rating.get());
            return new ResponseEntity<>(ratingResource, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Handle validation errors (e.g., ride not found)
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            // Handle business logic errors (e.g., ride not completed, already rated)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Retrieves all ratings for a specific driver.
     * @param driverId the driver user ID
     * @return list of ratings
     */
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<RatingResource>> getRatingsByDriver(@PathVariable UUID driverId) {
        var query = new GetRatingsByDriverUserIdQuery(driverId);
        var ratings = queryService.handle(query);
        
        if (ratings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        var resources = ratings.stream()
                .map(RatingResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(resources);
    }

    /**
     * Retrieves rating statistics for a specific driver.
     * @param driverId the driver user ID
     * @return driver rating statistics
     */
    @GetMapping("/driver/{driverId}/stats")
    public ResponseEntity<DriverRatingStatsResource> getDriverRatingStats(@PathVariable UUID driverId) {
        var driver = driverUserRepository.findById(driverId);
        if (driver.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var driverEntity = driver.get();
        var ratingsQuery = new GetRatingsByDriverUserIdQuery(driverId);
        var ratings = queryService.handle(ratingsQuery);
        
        // Get the 5 most recent ratings
        var recentRatings = ratings.stream()
                .sorted((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()))
                .limit(5)
                .map(RatingResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        
        var statsResource = new DriverRatingStatsResource(
                driverId,
                driverEntity.getFirstName() + " " + driverEntity.getLastName(),
                driverEntity.getAverageRating(),
                driverEntity.getTotalRatings(),
                recentRatings
        );
        
        return ResponseEntity.ok(statsResource);
    }

    /**
     * Retrieves the rating for a specific ride.
     * @param rideId the ride ID
     * @return the rating if found
     */
    @GetMapping("/ride/{rideId}")
    public ResponseEntity<RatingResource> getRatingByRide(@PathVariable UUID rideId) {
        var query = new GetRatingByRideIdQuery(rideId);
        var rating = queryService.handle(query);
        
        if (rating.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var resource = RatingResourceFromEntityAssembler.toResourceFromEntity(rating.get());
        return ResponseEntity.ok(resource);
    }

    /**
     * Retrieves all ratings given by a specific passenger.
     * @param passengerId the passenger user ID
     * @return list of ratings
     */
    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<List<RatingResource>> getRatingsByPassenger(@PathVariable UUID passengerId) {
        var query = new GetRatingsByPassengerUserIdQuery(passengerId);
        var ratings = queryService.handle(query);
        
        if (ratings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        var resources = ratings.stream()
                .map(RatingResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(resources);
    }
}
