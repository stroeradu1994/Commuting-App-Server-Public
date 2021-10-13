package com.commuting.commutingapp.trip.service.cache;

import com.commuting.commutingapp.common.dto.PointWithTimeDeltaAndDistanceDelta;
import com.commuting.commutingapp.common.wrapper.ExpirableHashMap;
import com.commuting.commutingapp.direction.model.Route;
import com.commuting.commutingapp.direction.service.api.PolylineProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PointsCache {

    @Autowired
    PolylineProcessor polylineProcessor;

    Map<String, LinkedHashSet<PointWithTimeDeltaAndDistanceDelta>> pointsCache = new ExpirableHashMap<>("points", 3);

    public boolean isInCache(String tripId) {
        return this.pointsCache.containsKey(tripId);
    }

    public LinkedHashSet<PointWithTimeDeltaAndDistanceDelta> getFromCache(String tripId) {
        return this.pointsCache.get(tripId);
    }

    public LinkedHashSet<PointWithTimeDeltaAndDistanceDelta> getFromCache(String tripId, Route route) {
//        if (!isInCache(tripId)) {
//            addToCache(tripId, polylineProcessor.processPolylines(route));
//        }
//        return getFromCache(tripId);
        return polylineProcessor.processPolylines(route);
    }

    private void addToCache(String tripId, LinkedHashSet<PointWithTimeDeltaAndDistanceDelta> pointWithTimeDeltaAndDistanceDeltas) {
        this.pointsCache.put(tripId, pointWithTimeDeltaAndDistanceDeltas);
    }
}
