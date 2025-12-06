package com.studentconnect.gouni.platform.carpooling.application.internal.commandservices;

import com.studentconnect.gouni.platform.carpooling.domain.model.aggregates.Rating;
import com.studentconnect.gouni.platform.carpooling.domain.model.commands.CreateRatingCommand;
import com.studentconnect.gouni.platform.carpooling.domain.model.valueobjects.RideStatus;
import com.studentconnect.gouni.platform.carpooling.domain.services.RatingCommandService;
import com.studentconnect.gouni.platform.carpooling.infrastructure.persistence.jpa.repositories.RatingRepository;
import com.studentconnect.gouni.platform.carpooling.infrastructure.persistence.jpa.repositories.RideRepository;
import com.studentconnect.gouni.platform.iam.infrastructure.persistance.jpa.repositories.DriverUserRepository;
import com.studentconnect.gouni.platform.iam.infrastructure.persistance.jpa.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RatingCommandServiceImpl implements RatingCommandService {
    private final RatingRepository ratingRepository;
    private final RideRepository rideRepository;
    private final DriverUserRepository driverUserRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Optional<Rating> handle(CreateRatingCommand command) {
        // 1. Verify ride exists
        var ride = rideRepository.findById(command.rideId());
        if (ride.isEmpty()) {
            throw new IllegalArgumentException("Ride with ID " + command.rideId() + " not found");
        }

        var rideEntity = ride.get();

        // 2. Verify ride is COMPLETED
        if (rideEntity.getRideStatus() != RideStatus.COMPLETED) {
            throw new IllegalStateException("Only COMPLETED rides can be rated. Current status: " + rideEntity.getRideStatus());
        }

        // 3. Verify rating doesn't already exist for this ride
        var existingRating = ratingRepository.findByRideId(command.rideId());
        if (existingRating.isPresent()) {
            throw new IllegalStateException("This ride has already been rated");
        }

        // 4. Create the rating
        var rating = new Rating(
                rideEntity,
                rideEntity.getDriverUser(),
                rideEntity.getPassengerUser(),
                command.score(),
                command.comment()
        );

        var savedRating = ratingRepository.save(rating);

        // 5. Update driver's average rating statistics
        updateDriverRatingStatistics(rideEntity.getDriverUser().getId());

        return Optional.of(savedRating);
    }

    /**
     * Updates the driver's average rating and total ratings count.
     * @param driverId the driver user ID
     */
    private void updateDriverRatingStatistics(java.util.UUID driverId) {
        var averageRating = ratingRepository.findAverageRatingByDriverId(driverId);
        var totalRatings = ratingRepository.countRatingsByDriverId(driverId);

        var driver = driverUserRepository.findById(driverId);
        if (driver.isPresent()) {
            var driverEntity = driver.get();
            driverEntity.setAverageRating(averageRating != null ? averageRating : 0.0);
            driverEntity.setTotalRatings(totalRatings);
            userRepository.save(driverEntity);
        }
    }
}
