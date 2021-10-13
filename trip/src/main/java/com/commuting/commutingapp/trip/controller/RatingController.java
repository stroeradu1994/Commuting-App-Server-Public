package com.commuting.commutingapp.trip.controller;

import com.commuting.commutingapp.trip.dto.statistic.RatingRequest;
import com.commuting.commutingapp.trip.service.api.RatingService;
import com.commuting.commutingapp.trip.service.api.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rating")
public class RatingController {

    @Autowired
    RatingService ratingService;

    @PostMapping()
    public ResponseEntity<?> rate(@RequestBody RatingRequest ratingRequest) {
        ratingService.rate(ratingRequest);
        return ResponseEntity.ok().build();
    }

}
