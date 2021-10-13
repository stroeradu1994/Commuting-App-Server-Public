package com.commuting.commutingapp.direction.service.cache;

import com.commuting.commutingapp.common.wrapper.ExpirableHashMap;
import com.commuting.commutingapp.direction.model.Route;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RouteCache {
    Map<String, List<Route>> routeCache = new ExpirableHashMap<>("route", 3);

    public void addToCache(List<Route> routeResults) {
        routeCache.put(UUID.randomUUID().toString(), routeResults);
    }

    public Optional<Route> getFromCache(String routeId) {
        return routeCache.values().stream().flatMap(Collection::stream).filter(route -> {
            return route.getId().equals(routeId);
        }).findFirst();
    }

}
