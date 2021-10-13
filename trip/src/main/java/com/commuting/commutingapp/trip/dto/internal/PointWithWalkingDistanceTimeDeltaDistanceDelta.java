package com.commuting.commutingapp.trip.dto.internal;

import com.commuting.commutingapp.common.dto.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointWithWalkingDistanceTimeDeltaDistanceDelta {
    Point point;
    double distance;
    double timeDelta;
    double distanceDelta;
}
