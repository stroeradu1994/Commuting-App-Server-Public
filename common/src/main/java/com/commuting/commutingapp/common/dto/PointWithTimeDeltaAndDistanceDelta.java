package com.commuting.commutingapp.common.dto;

import com.commuting.commutingapp.common.dto.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointWithTimeDeltaAndDistanceDelta {
    Point point;
    double timeDelta; // in seconds
    double distanceDelta; // in seconds
}