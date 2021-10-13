package com.commuting.commutingapp.location.dto.location.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLocationRequest {
    private String label;
    private String address;
    private double lat;
    private double lng;
}
