package com.commuting.commutingapp.trip.dto.trip.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerResponse {
    private String id;
    private String firstName;
    private String lastName;
    private double review;
}
