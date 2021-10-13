package com.commuting.commutingapp.direction.dto.internal;

import com.commuting.commutingapp.common.dto.Point;

import java.io.Serializable;

public class Step implements Serializable {
    public Point end_location;
    public Point start_location;
    public DoubleValue duration;
    public DoubleValue distance;
    public Polyline polyline;
}
