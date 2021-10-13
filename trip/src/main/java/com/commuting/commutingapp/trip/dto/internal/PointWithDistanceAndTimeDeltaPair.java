package com.commuting.commutingapp.trip.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointWithDistanceAndTimeDeltaPair {
    PointWithWalkingDistanceTimeDeltaDistanceDelta pickup;
    PointWithWalkingDistanceTimeDeltaDistanceDelta drop;
}
