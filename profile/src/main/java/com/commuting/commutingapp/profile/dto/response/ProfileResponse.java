package com.commuting.commutingapp.profile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String imageUrl;
    private double rating;
    private double points;
}
