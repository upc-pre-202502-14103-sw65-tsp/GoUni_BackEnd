package com.studentconnect.gouni.platform.carpooling.infrastructure.persistence.jpa.repositories;

import com.studentconnect.gouni.platform.carpooling.domain.model.aggregates.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {
    /**
     * Finds a rating by ride ID.
     * @param rideId the ride ID
     * @return the rating if found
     */
    Optional<Rating> findByRideId(UUID rideId);

    /**
     * Finds all ratings for a specific driver.
     * @param driverId the driver user ID
     * @return list of ratings
     */
    List<Rating> findByRatedDriverId(UUID driverId);

    /**
     * Finds all ratings given by a specific passenger.
     * @param passengerId the passenger user ID
     * @return list of ratings
     */
    List<Rating> findByRatingPassengerId(UUID passengerId);

    /**
     * Calculates the average rating for a driver.
     * @param driverId the driver user ID
     * @return the average rating
     */
    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.ratedDriver.id = :driverId")
    Double findAverageRatingByDriverId(@Param("driverId") UUID driverId);

    /**
     * Counts the total number of ratings for a driver.
     * @param driverId the driver user ID
     * @return the count of ratings
     */
    @Query("SELECT COUNT(r) FROM Rating r WHERE r.ratedDriver.id = :driverId")
    Long countRatingsByDriverId(@Param("driverId") UUID driverId);
}
