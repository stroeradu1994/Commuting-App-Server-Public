package com.commuting.commutingapp.direction.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeocodeResponse implements Serializable {
    List<GeocodeResult> results;
}
