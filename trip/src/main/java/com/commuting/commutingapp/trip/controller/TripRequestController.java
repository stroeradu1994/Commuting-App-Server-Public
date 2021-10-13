package com.commuting.commutingapp.trip.controller;

import com.commuting.commutingapp.trip.dto.triprequest.CreateTripRequestDto;
import com.commuting.commutingapp.trip.service.api.TripRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tripRequest")
public class TripRequestController {

    @Autowired
    TripRequestService tripRequestService;

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody CreateTripRequestDto createTripRequestDto) {
        return ResponseEntity.ok(tripRequestService.create(createTripRequestDto));
    }

    @GetMapping()
    public ResponseEntity<?> get() {
        return ResponseEntity.ok(tripRequestService.get());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") String id) {
        return ResponseEntity.ok(tripRequestService.get(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        tripRequestService.delete(id);
        return ResponseEntity.ok("Deleted");
    }
}
