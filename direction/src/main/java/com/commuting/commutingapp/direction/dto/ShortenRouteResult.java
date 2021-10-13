package com.commuting.commutingapp.direction.dto;

import com.google.maps.model.Bounds;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ShortenRouteResult {
    private String id;
    private double distance;
    private double duration;
    private String path;
}
