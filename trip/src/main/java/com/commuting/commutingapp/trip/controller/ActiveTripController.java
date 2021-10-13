package com.commuting.commutingapp.trip.controller;

import com.commuting.commutingapp.trip.dto.trip.ArrivedAtPickupRequest;
import com.commuting.commutingapp.trip.dto.trip.CompleteTripRequest;
import com.commuting.commutingapp.trip.dto.trip.DropPassengerRequest;
import com.commuting.commutingapp.trip.dto.trip.PickupPassengerRequest;
import com.commuting.commutingapp.trip.service.api.ActiveTripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trip/active")
public class ActiveTripController {

    @Autowired
    ActiveTripService activeTripService;

    @PostMapping("/complete")
    public ResponseEntity<?> complete(@RequestBody CompleteTripRequest completeTripRequest) {
        activeTripService.complete(completeTripRequest);
        return ResponseEntity.ok("Pickup");
    }

    @PostMapping("/arrivedAtPickup")
    public ResponseEntity<?> arrivedAtPickup(@RequestBody ArrivedAtPickupRequest arrivedAtPickupRequest) {
        activeTripService.arrivedAtPickup(arrivedAtPickupRequest);
        return ResponseEntity.ok("Pickup");
    }

    @PostMapping("/pickup")
    public ResponseEntity<?> pickup(@RequestBody PickupPassengerRequest pickupPassengerRequest) {
        activeTripService.pickup(pickupPassengerRequest);
        return ResponseEntity.ok("Pickup");
    }

    @PostMapping("/drop")
    public ResponseEntity<?> drop(@RequestBody DropPassengerRequest dropPassengerRequest) {
        activeTripService.drop(dropPassengerRequest);
        return ResponseEntity.ok("Drop");
    }

    @GetMapping("")
    public ResponseEntity<?> get() {
        return ResponseEntity.ok(activeTripService.getActive());
    }

    @GetMapping("/passenger/{id}")
    public ResponseEntity<?> getForPassenger(@PathVariable("id") String id) {
        return ResponseEntity.ok(activeTripService.getActiveForPassenger(id));
    }

    @GetMapping("/driver/{id}")
    public ResponseEntity<?> getForDriver(@PathVariable("id") String id) {
        return ResponseEntity.ok(activeTripService.getActiveForDriver(id));
    }
}
