package com.commuting.commutingapp.trip.dto.triprequest;

import lombok.Data;

@Data
public class CreateTripRequestDto {
    private String from;
    private String to;
    private String arriveBy;
    private boolean asap;
}
