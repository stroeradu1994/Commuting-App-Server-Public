package com.commuting.commutingapp.direction.service.api.impl;

import com.commuting.commutingapp.common.utils.DateTimeUtils;
import com.commuting.commutingapp.common.utils.LocationUtils;
import com.commuting.commutingapp.direction.api.DirectionApi;
import com.commuting.commutingapp.direction.dto.GetRoutesRequest;
import com.commuting.commutingapp.direction.dto.ShortenRouteResult;
import com.commuting.commutingapp.direction.dto.internal.DirectionsResult;
import com.commuting.commutingapp.direction.dto.internal.Step;
import com.commuting.commutingapp.direction.model.Route;
import com.commuting.commutingapp.direction.model.RouteStep;
import com.commuting.commutingapp.direction.repo.RouteStepRepo;
import com.commuting.commutingapp.direction.repo.RouteRepo;
import com.commuting.commutingapp.direction.service.api.DirectionService;
import com.commuting.commutingapp.direction.service.cache.RouteCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class DirectionServiceImpl implements DirectionService {

    @Autowired
    DirectionApi directionsApi;

    @Autowired
    RouteCache routeCache;

    @Autowired
    RouteRepo routeRepo;

    @Autowired
    RouteStepRepo routeStepRepo;

    @Override
    public List<ShortenRouteResult> getRoutes(GetRoutesRequest getRoutesRequest) {
        try {
            DirectionsResult directionsResult = directionsApi.getRoute(getRoutesRequest.getFrom(), getRoutesRequest.getTo(), DateTimeUtils.getInstantFromDateTimeString(getRoutesRequest.getLeaveAt()));
            List<Route> routeResults = Arrays.stream(directionsResult.routes)
                    .map(route -> new Route(
                            UUID.randomUUID().toString(),
                            route.legs.get(0).distance.value,
                            route.legs.get(0).duration_in_traffic.value,
                            route.legs.get(0).duration.value,
                            LocationUtils.locationToString(route.bounds.northeast.lat, route.bounds.northeast.lng),
                            LocationUtils.locationToString(route.bounds.southwest.lat, route.bounds.southwest.lng),
                            route.overview_polyline.points,
                            IntStream
                                    .range(0, route.legs.get(0).steps.size())
                                    .mapToObj(index -> {
                                        Step step = route.legs.get(0).steps.get(index);
                                        return new RouteStep(UUID.randomUUID().toString(), step.start_location.getLat(), step.start_location.getLng(), step.end_location.getLat(), step.end_location.getLng(), step.duration.value, step.distance.value, index, step.polyline.points);
                                    })
                                    .collect(Collectors.toSet())))
                    .collect(Collectors.toList());

            routeCache.addToCache(routeResults);

            return routeResults.stream()
                    .map(route -> new ShortenRouteResult(route.getId(), route.getDistance(), route.getDuration(), route.getPath()))
                    .collect(Collectors.toList());
        } catch (ParseException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public Route getRoute(String routeId) {
        Optional<Route> routeOptional = routeCache.getFromCache(routeId);
        return routeOptional.orElseGet(() -> routeRepo.findById(routeId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Route " + routeId + " not found!")));
    }

    @Override
    @Transactional
    public Route saveRoute(String routeId) {
        Route route = routeRepo.save(getRoute(routeId));
        route.addPoints(route.getPoints().stream().map(point -> {
                    point.setRoute(route);
                    return routeStepRepo.save(point);
                }).collect(Collectors.toSet())
        );
        return routeRepo.save(route);
    }

    @Override
    @Transactional
    public void deleteRoute(String routeId) {
        routeRepo.deleteById(routeId);
    }
}
