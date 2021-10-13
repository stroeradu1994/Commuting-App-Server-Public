package com.commuting.commutingapp.profile.controller;

import com.commuting.commutingapp.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    ProfileService profileService;

    @GetMapping()
    public ResponseEntity<?> get() {
        return ResponseEntity.ok(profileService.getProfile());
    }
}
