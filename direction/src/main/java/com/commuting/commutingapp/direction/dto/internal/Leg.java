package com.commuting.commutingapp.direction.dto.internal;

import java.io.Serializable;
import java.util.List;

public class Leg implements Serializable {
    public DoubleValue duration_in_traffic;
    public DoubleValue duration;
    public DoubleValue distance;
    public List<Step> steps;
}
