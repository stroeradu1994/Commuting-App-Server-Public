package com.commuting.commutingapp.location.dto.location.response;

import com.commuting.commutingapp.common.dto.Point;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationResponse {
    private String id;
    private String label;
    private String address;
    private Point point;
}
