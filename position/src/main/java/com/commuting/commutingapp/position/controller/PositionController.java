package com.commuting.commutingapp.position.controller;


import com.commuting.commutingapp.position.dto.PositionDto;
import com.commuting.commutingapp.position.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/position")
public class PositionController {

    @Autowired
    PositionService positionService;

    @PostMapping()
    public ResponseEntity<?> add(@RequestBody PositionDto positionDto) {
        positionService.add(positionDto);
        return ResponseEntity.ok().build();
    }

}
