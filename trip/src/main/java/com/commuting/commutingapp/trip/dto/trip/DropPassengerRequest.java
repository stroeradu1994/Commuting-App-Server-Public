package com.commuting.commutingapp.trip.dto.trip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DropPassengerRequest {
    private String tripId;
    private String stopId;
}
