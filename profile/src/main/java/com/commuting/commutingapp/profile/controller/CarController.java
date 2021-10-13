package com.commuting.commutingapp.profile.controller;

import com.commuting.commutingapp.profile.dto.request.CreateCarRequest;
import com.commuting.commutingapp.profile.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/car")
public class CarController {

    @Autowired
    CarService carService;

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody CreateCarRequest createCarRequest) {
        return ResponseEntity.ok(carService.createCar(createCarRequest));
    }

    @GetMapping()
    public ResponseEntity<?> get() {
        return ResponseEntity.ok(carService.getCars());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        carService.deleteCar(id);
        return ResponseEntity.ok().build();
    }
}
