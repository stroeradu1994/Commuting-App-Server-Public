package com.commuting.commutingapp.trip.repo;

import com.commuting.commutingapp.trip.model.TripRequest;
import com.commuting.commutingapp.trip.model.TripState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TripRequestRepo extends JpaRepository<TripRequest, String> {

    List<TripRequest> findAllByConfirmedAndBoundNeLatLessThanAndBoundNeLngLessThanAndBoundSwLatGreaterThanAndBoundSwLngGreaterThanAndArriveByGreaterThanAndArriveByLessThan(boolean confirmed, double neLat, double neLng, double swLat, double swLng, LocalDateTime arriveByGreater, LocalDateTime arriveByLess);

    List<TripRequest> findAllByUserIdAndConfirmed(String userId, boolean confirmed);

    List<TripRequest> findAllByConfirmedAndArriveByLessThan(boolean confirmed, LocalDateTime arriveBy);

    boolean existsByUserIdAndConfirmedAndArriveByGreaterThanAndArriveByLessThan(String userId, boolean confirmed, LocalDateTime leaveAt, LocalDateTime arriveAt);

}
