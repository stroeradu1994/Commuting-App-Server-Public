package com.commuting.commutingapp.trip.dto.trip.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StopResponse {
    private String id;
    private String point;
    private String address;
    private boolean pickup;
    private boolean arrived;
    private String time;
    private String passengerId;
    private String passengerName;
    private boolean confirmed;
}
