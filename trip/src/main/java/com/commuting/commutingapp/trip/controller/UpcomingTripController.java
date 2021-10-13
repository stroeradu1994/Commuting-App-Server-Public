package com.commuting.commutingapp.trip.controller;

import com.commuting.commutingapp.trip.dto.trip.ConfirmTripRequest;
import com.commuting.commutingapp.trip.dto.trip.CreateTripDto;
import com.commuting.commutingapp.trip.dto.trip.StartTripRequest;
import com.commuting.commutingapp.trip.service.api.UpcomingTripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trip/upcoming")
public class UpcomingTripController {

    @Autowired
    UpcomingTripService upcomingTripService;

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody CreateTripDto createTripDto) throws Exception {
        return ResponseEntity.ok(upcomingTripService.create(createTripDto));
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestBody ConfirmTripRequest confirmTripRequest) {
        upcomingTripService.confirm(confirmTripRequest);
        return ResponseEntity.ok("Confirmed");
    }

    @PostMapping("/start")
    public ResponseEntity<?> start(@RequestBody StartTripRequest startTripRequest) {
        upcomingTripService.start(startTripRequest.getTripId());
        return ResponseEntity.ok("Start");
    }

    @GetMapping()
    public ResponseEntity<?> getTrips() {
        return ResponseEntity.ok(upcomingTripService.getUpcoming());
    }

    @GetMapping("/driver/{id}")
    public ResponseEntity<?> getForDriver(@PathVariable("id") String id) {
        return ResponseEntity.ok(upcomingTripService.getUpcomingForDriver(id));
    }

    @GetMapping("/passenger/{id}")
    public ResponseEntity<?> getForPassenger(@PathVariable("id") String id) {
        return ResponseEntity.ok(upcomingTripService.getUpcomingForPassenger(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancel(@PathVariable("id") String id) {
        upcomingTripService.cancel(id);
        return ResponseEntity.ok("Deleted");
    }
}
