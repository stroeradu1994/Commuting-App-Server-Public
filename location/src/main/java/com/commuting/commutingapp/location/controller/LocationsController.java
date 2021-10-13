package com.commuting.commutingapp.location.controller;

import com.commuting.commutingapp.location.dto.location.request.CreateLocationRequest;
import com.commuting.commutingapp.location.dto.location.request.UpdateLocationRequest;
import com.commuting.commutingapp.location.service.api.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/location")
public class LocationsController {

    @Autowired
    LocationService locationService;

    @PostMapping()
    public ResponseEntity<?> createLocation(@RequestBody CreateLocationRequest createLocationRequest) {
        return ResponseEntity.ok(locationService.createLocation(createLocationRequest));
    }

    @PutMapping()
    public ResponseEntity<?> updateLocation(@RequestBody UpdateLocationRequest updateLocationRequest) throws Exception {
        return ResponseEntity.ok(locationService.updateLocation(updateLocationRequest));
    }

    @GetMapping()
    public ResponseEntity<?> getLocations() {
        return ResponseEntity.ok(locationService.getLocations());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLocation(@PathVariable("id") String id) {
        locationService.deleteLocation(id);
        return ResponseEntity.ok("Deleted");
    }
}
