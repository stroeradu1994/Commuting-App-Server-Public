package com.commuting.commutingapp.location.dto.location.internal;

import com.commuting.commutingapp.location.model.Location;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationPair {
    Location from;
    Location to;
}
