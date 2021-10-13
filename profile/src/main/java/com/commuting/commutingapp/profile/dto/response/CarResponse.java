package com.commuting.commutingapp.profile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarResponse {
    private String id;
    private String brand;
    private String model;
    private String color;
    private String plate;
}
