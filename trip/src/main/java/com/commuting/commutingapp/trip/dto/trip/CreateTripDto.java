package com.commuting.commutingapp.trip.dto.trip;

import lombok.Data;

@Data
public class CreateTripDto {
    private String from;
    private String to;
    private String leaveAt;
    private String routeId;
    private String carId;
}
