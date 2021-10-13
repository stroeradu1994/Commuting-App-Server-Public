package com.commuting.commutingapp.location.service.api;

import com.commuting.commutingapp.location.dto.location.internal.LocationPair;
import com.commuting.commutingapp.location.dto.location.request.CreateLocationRequest;
import com.commuting.commutingapp.location.dto.location.request.UpdateLocationRequest;
import com.commuting.commutingapp.location.dto.location.response.LocationResponse;
import com.commuting.commutingapp.location.model.Location;

import java.util.List;

public interface LocationService {

    LocationResponse createLocation(CreateLocationRequest createLocationRequest);

    LocationResponse updateLocation(UpdateLocationRequest updateLocationRequest);

    List<LocationResponse> getLocations();

    Location getLocation(String id);

    LocationPair getLocationPair(String from, String to);

    void deleteLocation(String id);
}
