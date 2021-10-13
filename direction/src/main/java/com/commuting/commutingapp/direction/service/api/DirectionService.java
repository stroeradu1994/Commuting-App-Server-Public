package com.commuting.commutingapp.direction.service.api;

import com.commuting.commutingapp.direction.dto.GetRoutesRequest;
import com.commuting.commutingapp.direction.dto.ShortenRouteResult;
import com.commuting.commutingapp.direction.model.Route;

import java.util.List;

public interface DirectionService {
    List<ShortenRouteResult> getRoutes(GetRoutesRequest getRoutesRequest);

    Route getRoute(String routeId);

    Route saveRoute(String routeId);

    void deleteRoute(String routeId);

}
