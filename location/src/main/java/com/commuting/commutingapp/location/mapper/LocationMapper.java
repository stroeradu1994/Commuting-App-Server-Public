package com.commuting.commutingapp.location.mapper;

import com.commuting.commutingapp.location.dto.location.response.LocationResponse;
import com.commuting.commutingapp.location.model.Location;

public class LocationMapper {

    public static LocationResponse buildLocationResponse(Location location) {
        return new LocationResponse(location.getId(), location.getLabel(), location.getAddress(), location.getPoint());
    }
}
