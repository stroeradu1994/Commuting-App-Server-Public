package com.commuting.commutingapp.trip.repo;

import com.commuting.commutingapp.trip.model.Trip;
import com.commuting.commutingapp.trip.model.TripState;
import com.commuting.commutingapp.trip.model.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TripRepo extends JpaRepository<Trip, String> {

    List<Trip> findAllByStateAndBoundNeLatGreaterThanAndBoundNeLngGreaterThanAndBoundSwLatLessThanAndBoundSwLngLessThanAndArriveAtLessThanAndArriveAtGreaterThan(TripState state, double neLat, double neLng, double swLat, double swLng, LocalDateTime arriveAtLess, LocalDateTime arriveAtGreater);

    List<Trip> findAllByStateAndDriverId(TripState state, String driverId);

    List<Trip> findAllByStateAndPassengersUserIdAndPassengersCompleted(TripState state, String userId, boolean completed);

    List<Trip> findAllByStateAndPassengersUserId(TripState state, String userId);

    List<Trip> findAllByStatusAndMatchesUserId(TripStatus status, String userId);

    boolean existsByStateAndDriverIdAndLeaveAtGreaterThanAndArriveAtLessThan(TripState tripState, String driverId, LocalDateTime leaveAt, LocalDateTime arriveAt);

    boolean existsByStateAndPassengersUserIdAndLeaveAtGreaterThanAndArriveAtLessThan(TripState tripState, String userId, LocalDateTime leaveAt, LocalDateTime arriveAt);

    List<Trip> findAllByStatusAndLeaveAtLessThan(TripStatus status, LocalDateTime leaveAt);

}
