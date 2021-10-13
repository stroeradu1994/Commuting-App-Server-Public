package com.commuting.commutingapp.trip.controller;

import com.commuting.commutingapp.trip.service.api.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    StatisticsService statisticsService;

    @GetMapping("")
    public ResponseEntity<?> getStatistics() {
        return ResponseEntity.ok(statisticsService.get());
    }

}
