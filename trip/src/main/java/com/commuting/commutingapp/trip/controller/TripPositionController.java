package com.commuting.commutingapp.trip.controller;

import com.commuting.commutingapp.trip.service.api.TripPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tripPosition")
public class TripPositionController {

    @Autowired
    TripPositionService tripPositionService;


    @GetMapping("/{tripId}/{userId}")
    public ResponseEntity<?> get(@PathVariable("tripId") String tripId, @PathVariable("userId") String userId) {
        return ResponseEntity.ok(tripPositionService.get(tripId, userId));
    }

}
