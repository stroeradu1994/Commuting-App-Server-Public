package com.commuting.commutingapp.trip.dto.trip.past;

import com.commuting.commutingapp.location.dto.location.response.LocationResponse;
import com.commuting.commutingapp.trip.dto.trip.common.PassengerResponse;
import com.commuting.commutingapp.trip.dto.trip.common.StopResponse;
import com.commuting.commutingapp.trip.model.TripStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PastDriverTripResponse {
    private String tripId;
    private String carBrand;
    private String carModel;
    private String carColor;
    private String carPlate;
    private LocationResponse fromLocation;
    private LocationResponse toLocation;
    private String leaveTime;
    private String arriveTime;
    private String polyline;
    private TripStatus status;
    private List<PassengerResponse> passengers;
    private List<StopResponse> stops;
    private float rating;
}
