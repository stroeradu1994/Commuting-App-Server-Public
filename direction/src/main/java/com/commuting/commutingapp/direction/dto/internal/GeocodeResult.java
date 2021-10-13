package com.commuting.commutingapp.direction.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeocodeResult implements Serializable {
    String formatted_address;

}
