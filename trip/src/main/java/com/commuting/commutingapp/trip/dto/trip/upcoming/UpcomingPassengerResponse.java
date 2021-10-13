package com.commuting.commutingapp.trip.dto.trip.upcoming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpcomingPassengerResponse {
    private String passengerId;
    private String firstName;
    private String lastName;
    private String pickupPoint;
    private String dropPoint;
    private String pickupTime;
    private String dropTime;
    private double rating;
}
