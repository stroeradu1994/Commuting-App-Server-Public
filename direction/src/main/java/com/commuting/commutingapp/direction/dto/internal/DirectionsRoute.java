package com.commuting.commutingapp.direction.dto.internal;

import com.google.maps.model.Bounds;

import java.io.Serializable;
import java.util.List;

public class DirectionsRoute implements Serializable {
    private static final long serialVersionUID = 1L;
    public Polyline overview_polyline;
    public Bounds bounds;
    public List<Leg> legs;
}
