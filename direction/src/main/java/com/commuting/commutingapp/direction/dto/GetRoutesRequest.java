package com.commuting.commutingapp.direction.dto;

import com.commuting.commutingapp.common.dto.Point;
import lombok.Data;

@Data
public class GetRoutesRequest {
    private Point from;
    private Point to;
    private String leaveAt;
}
