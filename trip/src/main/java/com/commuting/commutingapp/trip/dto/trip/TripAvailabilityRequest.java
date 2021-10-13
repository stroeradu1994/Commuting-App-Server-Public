package com.commuting.commutingapp.trip.dto.trip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripAvailabilityRequest {
    private String dateTime;
    private boolean driver;
    private boolean asap;
}
