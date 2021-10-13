package com.commuting.commutingapp.direction.api;

import com.commuting.commutingapp.common.dto.Point;
import com.commuting.commutingapp.direction.dto.internal.DirectionsResult;

import java.time.Instant;

public interface DirectionApi {
    DirectionsResult getRoute(Point from, Point to, Instant leaveAt);
}
