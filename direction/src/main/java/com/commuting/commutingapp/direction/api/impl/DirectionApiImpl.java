package com.commuting.commutingapp.direction.api.impl;

import com.commuting.commutingapp.common.dto.Point;
import com.commuting.commutingapp.common.utils.LocationUtils;
import com.commuting.commutingapp.direction.api.DirectionApi;
import com.commuting.commutingapp.direction.dto.internal.DirectionsResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
public class DirectionApiImpl implements DirectionApi {

    private final static String DIRECTIONS_API = "https://maps.googleapis.com/maps/api/directions/json";
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${GOOGLE_KEY}")
    private String googleKey;

    @Override
    public DirectionsResult getRoute(Point from, Point to, Instant leaveAt) {
        String url = DIRECTIONS_API + "?origin=" + LocationUtils.locationToString(from) + "&destination=" + LocationUtils.locationToString(to) + "&alternatives=true&departure_time=" + leaveAt.getEpochSecond() + "&key=" + googleKey;
        return restTemplate.getForEntity(url, DirectionsResult.class).getBody();
    }
}
