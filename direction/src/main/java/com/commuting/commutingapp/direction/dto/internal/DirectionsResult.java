package com.commuting.commutingapp.direction.dto.internal;

import java.io.Serializable;

public class DirectionsResult implements Serializable {
    public GeocodedWaypoint[] geocodedWaypoints;
    public DirectionsRoute[] routes;
}

