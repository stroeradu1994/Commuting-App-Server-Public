package com.commuting.commutingapp.direction.service.api;

import com.commuting.commutingapp.common.dto.PointWithTimeDeltaAndDistanceDelta;
import com.commuting.commutingapp.direction.model.Route;

import java.util.LinkedHashSet;

public interface PolylineProcessor {
    LinkedHashSet<PointWithTimeDeltaAndDistanceDelta> processPolylines(Route route);
}
