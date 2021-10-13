package com.commuting.commutingapp.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Point implements Serializable {
    private double lat;
    private double lng;

    @Override
    public String toString() {
        return lat + ", " + lng;
    }
}
