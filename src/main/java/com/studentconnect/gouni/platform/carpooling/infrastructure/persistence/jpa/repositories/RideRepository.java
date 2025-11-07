package com.studentconnect.gouni.platform.carpooling.infrastructure.persistence.jpa.repositories;

import com.studentconnect.gouni.platform.carpooling.domain.model.aggregates.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RideRepository extends JpaRepository<Ride, UUID> {
    List<Ride> findAllByPassengerUser_Id(UUID passengerUserId);

    List<Ride> findAllByDriverUser_Id(UUID driverUserId);
}
