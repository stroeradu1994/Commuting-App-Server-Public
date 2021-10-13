package com.commuting.commutingapp.trip.controller;

import com.commuting.commutingapp.trip.service.api.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/match")
public class MatchController {

    @Autowired
    MatchService matchService;

    @PostMapping("/{id}/match")
    public ResponseEntity<?> match(@PathVariable("id") String id) {
        matchService.match(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/unmatch")
    public ResponseEntity<?> unmatch(@PathVariable("id") String id) {
        matchService.unmatch(id);
        return ResponseEntity.ok().build();
    }
}
