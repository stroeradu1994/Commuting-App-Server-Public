package com.commuting.commutingapp.trip.controller;

import com.commuting.commutingapp.trip.dto.trip.TripAvailabilityRequest;
import com.commuting.commutingapp.trip.dto.triprequest.CreateTripRequestDto;
import com.commuting.commutingapp.trip.service.api.MatchService;
import com.commuting.commutingapp.trip.service.api.TripAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/availability")
public class TripAvailabilityController {

    @Autowired
    TripAvailabilityService tripAvailabilityService;

    @PostMapping("")
    public ResponseEntity<?> checkAvailability(@RequestBody TripAvailabilityRequest tripAvailabilityRequest) {
        tripAvailabilityService.checkAvailability(tripAvailabilityRequest);
        return ResponseEntity.ok().build();
    }

}
