package com.commuting.commutingapp.direction.api.impl;

import com.commuting.commutingapp.common.dto.Point;
import com.commuting.commutingapp.common.utils.LocationUtils;
import com.commuting.commutingapp.direction.api.AddressApi;
import com.commuting.commutingapp.direction.dto.internal.GeocodeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AddressApiImpl implements AddressApi {

    private final static String GEOCODE_API = "https://maps.googleapis.com/maps/api/geocode/json";
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${GOOGLE_KEY}")
    private String googleKey;

    @Override
    public String getAddress(Point point) {
        String url = GEOCODE_API + "?latlng=" + LocationUtils.locationToString(point) + "&key=" + googleKey;
        GeocodeResponse geocodeResponse = restTemplate.getForEntity(url, GeocodeResponse.class).getBody();
        return geocodeResponse == null ? "-" : geocodeResponse.getResults().get(0).getFormatted_address();
    }
}
