package com.commuting.commutingapp.trip.dto.trip;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmTripRequest {
    private String tripId;
    private String leaveAt;
}
