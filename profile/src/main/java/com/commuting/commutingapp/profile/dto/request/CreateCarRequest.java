package com.commuting.commutingapp.profile.dto.request;

import lombok.Data;

@Data
public class CreateCarRequest {
    private String brand;
    private String model;
    private String color;
    private String plate;
}
