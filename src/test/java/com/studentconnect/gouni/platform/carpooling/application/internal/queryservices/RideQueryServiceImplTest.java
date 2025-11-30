package com.studentconnect.gouni.platform.carpooling.application.internal.queryservices;

import com.studentconnect.gouni.platform.carpooling.domain.model.aggregates.Ride;
import com.studentconnect.gouni.platform.carpooling.domain.model.queries.*;
import com.studentconnect.gouni.platform.carpooling.domain.model.valueobjects.RideStatus;
import com.studentconnect.gouni.platform.carpooling.infrastructure.persistence.jpa.repositories.RideRepository;
import com.studentconnect.gouni.platform.iam.domain.model.aggregates.DriverUser;
import com.studentconnect.gouni.platform.iam.domain.model.aggregates.PassengerUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("RideQueryServiceImpl Tests")
class RideQueryServiceImplTest {

    @Mock
    private RideRepository rideRepository;

    @InjectMocks
    private RideQueryServiceImpl rideQueryService;

    private UUID testRideId;
    private UUID testDriverUserId;
    private UUID testPassengerUserId;
    private DriverUser mockDriverUser;
    private PassengerUser mockPassengerUser;
    private Ride mockRide;
    private List<Ride> mockRideList;

    @BeforeEach
    void setUp() {
        testRideId = UUID.randomUUID();
        testDriverUserId = UUID.randomUUID();
        testPassengerUserId = UUID.randomUUID();

        mockDriverUser = new DriverUser(
                "driver@upc.edu.pe",
                "password123",
                "John",
                "Doe",
                "987654321",
                "https://example.com/photo.jpg",
                "12345678",
                "LIC123456",
                "Experienced driver",
                new ArrayList<>()
        );

        mockPassengerUser = new PassengerUser(
                "passenger@upc.edu.pe",
                "password123",
                "Jane",
                "Smith",
                "987654322",
                "https://example.com/photo2.jpg",
                "87654321",
                new ArrayList<>()
        );

        mockRide = new Ride(
                mockDriverUser,
                mockPassengerUser,
                "Lima, Peru",
                "Callao, Peru"
        );
        mockRide.setRideStatus(RideStatus.REQUESTED);

        mockRideList = new ArrayList<>();
        mockRideList.add(mockRide);
    }

    @Test
    @DisplayName("Should handle GetRideByIdQuery successfully")
    void shouldHandleGetRideByIdQuerySuccessfully() {
        when(rideRepository.findById(testRideId))
                .thenReturn(Optional.of(mockRide));

        var query = new GetRideByIdQuery(testRideId);
        var result = rideQueryService.handle(query);

        assertTrue(result.isPresent());
        assertNotNull(result.get());
        assertEquals(RideStatus.REQUESTED, result.get().getRideStatus());
        assertNotNull(result.get().getDriverUser());
        assertNotNull(result.get().getPassengerUser());
        verify(rideRepository, times(1)).findById(testRideId);
    }

    @Test
    @DisplayName("Should return empty Optional when ride not found by id")
    void shouldReturnEmptyOptionalWhenRideNotFoundById() {
        when(rideRepository.findById(testRideId))
                .thenReturn(Optional.empty());

        var query = new GetRideByIdQuery(testRideId);
        var result = rideQueryService.handle(query);

        assertTrue(result.isEmpty());
        verify(rideRepository, times(1)).findById(testRideId);
    }

    @Test
    @DisplayName("Should handle GetAllRidesByPassengerUserIdQuery successfully")
    void shouldHandleGetAllRidesByPassengerUserIdQuerySuccessfully() {
        when(rideRepository.findAllByPassengerUser_Id(testPassengerUserId))
                .thenReturn(mockRideList);

        var query = new GetAllRidesByPassengerUserIdQuery(testPassengerUserId);
        var result = rideQueryService.handle(query);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.get(0).getPassengerUser());
        assertEquals("passenger@upc.edu.pe", result.get(0).getPassengerUser().getEmail());
        verify(rideRepository, times(1)).findAllByPassengerUser_Id(testPassengerUserId);
    }

    @Test
    @DisplayName("Should return empty list when no rides found for passenger")
    void shouldReturnEmptyListWhenNoRidesFoundForPassenger() {
        when(rideRepository.findAllByPassengerUser_Id(testPassengerUserId))
                .thenReturn(Collections.emptyList());

        var query = new GetAllRidesByPassengerUserIdQuery(testPassengerUserId);
        var result = rideQueryService.handle(query);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(rideRepository, times(1)).findAllByPassengerUser_Id(testPassengerUserId);
    }

    @Test
    @DisplayName("Should handle GetAllRidesByDriverUserIdQuery successfully")
    void shouldHandleGetAllRidesByDriverUserIdQuerySuccessfully() {
        when(rideRepository.findAllByDriverUser_Id(testDriverUserId))
                .thenReturn(mockRideList);

        var query = new GetAllRidesByDriverUserIdQuery(testDriverUserId);
        var result = rideQueryService.handle(query);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.get(0).getDriverUser());
        assertEquals("driver@upc.edu.pe", result.get(0).getDriverUser().getEmail());
        verify(rideRepository, times(1)).findAllByDriverUser_Id(testDriverUserId);
    }

    @Test
    @DisplayName("Should return empty list when no rides found for driver")
    void shouldReturnEmptyListWhenNoRidesFoundForDriver() {
        when(rideRepository.findAllByDriverUser_Id(testDriverUserId))
                .thenReturn(Collections.emptyList());

        var query = new GetAllRidesByDriverUserIdQuery(testDriverUserId);
        var result = rideQueryService.handle(query);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(rideRepository, times(1)).findAllByDriverUser_Id(testDriverUserId);
    }

    @Test
    @DisplayName("Should handle GetAllRidesQuery successfully")
    void shouldHandleGetAllRidesQuerySuccessfully() {
        when(rideRepository.findAll())
                .thenReturn(mockRideList);

        var query = new GetAllRidesQuery();
        var result = rideQueryService.handle(query);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(rideRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no rides exist")
    void shouldReturnEmptyListWhenNoRidesExist() {
        when(rideRepository.findAll())
                .thenReturn(Collections.emptyList());

        var query = new GetAllRidesQuery();
        var result = rideQueryService.handle(query);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(rideRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should handle GetAllRidesByStatusQuery successfully with REQUESTED status")
    void shouldHandleGetAllRidesByStatusQuerySuccessfullyWithRequestedStatus() {
        when(rideRepository.findAllByRideStatus(RideStatus.REQUESTED))
                .thenReturn(mockRideList);

        var query = new GetAllRidesByStatusQuery(RideStatus.REQUESTED);
        var result = rideQueryService.handle(query);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(RideStatus.REQUESTED, result.get(0).getRideStatus());
        verify(rideRepository, times(1)).findAllByRideStatus(RideStatus.REQUESTED);
    }

    @Test
    @DisplayName("Should handle GetAllRidesByStatusQuery successfully with ACCEPTED status")
    void shouldHandleGetAllRidesByStatusQuerySuccessfullyWithAcceptedStatus() {
        Ride acceptedRide = new Ride(
                mockDriverUser,
                mockPassengerUser,
                "Lima, Peru",
                "Callao, Peru"
        );
        acceptedRide.setRideStatus(RideStatus.ACCEPTED);

        List<Ride> acceptedRides = new ArrayList<>();
        acceptedRides.add(acceptedRide);

        when(rideRepository.findAllByRideStatus(RideStatus.ACCEPTED))
                .thenReturn(acceptedRides);

        var query = new GetAllRidesByStatusQuery(RideStatus.ACCEPTED);
        var result = rideQueryService.handle(query);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(RideStatus.ACCEPTED, result.get(0).getRideStatus());
        verify(rideRepository, times(1)).findAllByRideStatus(RideStatus.ACCEPTED);
    }

    @Test
    @DisplayName("Should handle GetAllRidesByStatusQuery successfully with COMPLETED status")
    void shouldHandleGetAllRidesByStatusQuerySuccessfullyWithCompletedStatus() {
        Ride completedRide = new Ride(
                mockDriverUser,
                mockPassengerUser,
                "Lima, Peru",
                "Callao, Peru"
        );
        completedRide.setRideStatus(RideStatus.COMPLETED);

        List<Ride> completedRides = new ArrayList<>();
        completedRides.add(completedRide);

        when(rideRepository.findAllByRideStatus(RideStatus.COMPLETED))
                .thenReturn(completedRides);

        var query = new GetAllRidesByStatusQuery(RideStatus.COMPLETED);
        var result = rideQueryService.handle(query);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(RideStatus.COMPLETED, result.get(0).getRideStatus());
        verify(rideRepository, times(1)).findAllByRideStatus(RideStatus.COMPLETED);
    }

    @Test
    @DisplayName("Should return empty list when no rides found for status")
    void shouldReturnEmptyListWhenNoRidesFoundForStatus() {
        when(rideRepository.findAllByRideStatus(RideStatus.CANCELLED))
                .thenReturn(Collections.emptyList());

        var query = new GetAllRidesByStatusQuery(RideStatus.CANCELLED);
        var result = rideQueryService.handle(query);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(rideRepository, times(1)).findAllByRideStatus(RideStatus.CANCELLED);
    }

    @Test
    @DisplayName("Should handle GetAllRidesByLocationQuery successfully with pickUp location")
    void shouldHandleGetAllRidesByLocationQuerySuccessfullyWithPickUpLocation() {
        String location = "Lima, Peru";
        when(rideRepository.findAllByDropOffGeoLocationOrPickUpGeoLocation(location, location))
                .thenReturn(mockRideList);

        var query = new GetAllRidesByLocationQuery(location);
        var result = rideQueryService.handle(query);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(rideRepository, times(1))
                .findAllByDropOffGeoLocationOrPickUpGeoLocation(location, location);
    }

    @Test
    @DisplayName("Should handle GetAllRidesByLocationQuery successfully with dropOff location")
    void shouldHandleGetAllRidesByLocationQuerySuccessfullyWithDropOffLocation() {
        String location = "Callao, Peru";
        when(rideRepository.findAllByDropOffGeoLocationOrPickUpGeoLocation(location, location))
                .thenReturn(mockRideList);

        var query = new GetAllRidesByLocationQuery(location);
        var result = rideQueryService.handle(query);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(rideRepository, times(1))
                .findAllByDropOffGeoLocationOrPickUpGeoLocation(location, location);
    }

    @Test
    @DisplayName("Should return empty list when no rides found for location")
    void shouldReturnEmptyListWhenNoRidesFoundForLocation() {
        String location = "Arequipa, Peru";
        when(rideRepository.findAllByDropOffGeoLocationOrPickUpGeoLocation(location, location))
                .thenReturn(Collections.emptyList());

        var query = new GetAllRidesByLocationQuery(location);
        var result = rideQueryService.handle(query);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(rideRepository, times(1))
                .findAllByDropOffGeoLocationOrPickUpGeoLocation(location, location);
    }

    @Test
    @DisplayName("Should handle GetAllRidesByLocationQuery with multiple rides")
    void shouldHandleGetAllRidesByLocationQueryWithMultipleRides() {
        String location = "Lima, Peru";
        
        Ride ride2 = new Ride(
                mockDriverUser,
                mockPassengerUser,
                "Lima, Peru",
                "Miraflores, Peru"
        );
        ride2.setRideStatus(RideStatus.ACCEPTED);

        List<Ride> multipleRides = new ArrayList<>();
        multipleRides.add(mockRide);
        multipleRides.add(ride2);

        when(rideRepository.findAllByDropOffGeoLocationOrPickUpGeoLocation(location, location))
                .thenReturn(multipleRides);

        var query = new GetAllRidesByLocationQuery(location);
        var result = rideQueryService.handle(query);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(rideRepository, times(1))
                .findAllByDropOffGeoLocationOrPickUpGeoLocation(location, location);
    }

    @Test
    @DisplayName("Should handle GetAllRidesByLocationQuery with empty location string")
    void shouldHandleGetAllRidesByLocationQueryWithEmptyLocationString() {
        String emptyLocation = "";
        when(rideRepository.findAllByDropOffGeoLocationOrPickUpGeoLocation(emptyLocation, emptyLocation))
                .thenReturn(Collections.emptyList());

        var query = new GetAllRidesByLocationQuery(emptyLocation);
        var result = rideQueryService.handle(query);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(rideRepository, times(1))
                .findAllByDropOffGeoLocationOrPickUpGeoLocation(emptyLocation, emptyLocation);
    }
}

