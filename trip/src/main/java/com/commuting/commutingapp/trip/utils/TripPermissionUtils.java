package com.commuting.commutingapp.trip.utils;

import com.commuting.commutingapp.common.utils.DateTimeUtils;
import com.commuting.commutingapp.trip.model.Passenger;
import com.commuting.commutingapp.trip.model.Stop;
import com.commuting.commutingapp.trip.model.Trip;
import com.commuting.commutingapp.trip.model.TripStatus;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

public class TripPermissionUtils {

    public static boolean isAlmostPickupOrRiding(Stop pickup) {
        return pickup.getTime().isAfter(DateTimeUtils.getTimeNow().minusMinutes(10));
    }

    public static boolean isActiveTrip(Trip trip) {
        return trip.getStatus() == TripStatus.ACTIVE;
    }

    public static boolean isDriver(String userId, Trip trip) {
        return trip.getDriverId().equals(userId);
    }

    public static boolean isCurrentUserPassenger(String currentUserId, Set<Passenger> passengers) {
        return passengers.stream().map(Passenger::getUserId).anyMatch(p -> p.equals(currentUserId));
    }
}
