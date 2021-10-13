package com.commuting.commutingapp.trip.controller;

import com.commuting.commutingapp.trip.service.api.PastTripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trip/past")
public class PastTripController {

    @Autowired
    PastTripService pastTripService;

    @GetMapping("")
    public ResponseEntity<?> getPast() {
        return ResponseEntity.ok(pastTripService.getPast());
    }

    @GetMapping("/passenger/{id}")
    public ResponseEntity<?> getPastForPassenger(@PathVariable("id") String id) {
        return ResponseEntity.ok(pastTripService.getPastForPassenger(id));
    }

    @GetMapping("/driver/{id}")
    public ResponseEntity<?> getPastForDriver(@PathVariable("id") String id) {
        return ResponseEntity.ok(pastTripService.getPastForDriver(id));
    }

}
