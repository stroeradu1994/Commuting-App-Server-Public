package com.commuting.commutingapp.direction.controller;

import com.commuting.commutingapp.direction.dto.GetRoutesRequest;
import com.commuting.commutingapp.direction.service.api.DirectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/direction")
public class DirectionController {

    @Autowired
    DirectionService directionService;

    @PostMapping("")
    public ResponseEntity<?> getRoutes(@RequestBody GetRoutesRequest getRoutesRequest) {
        return ResponseEntity.ok(directionService.getRoutes(getRoutesRequest));
    }
}
