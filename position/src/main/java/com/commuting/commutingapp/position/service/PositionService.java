package com.commuting.commutingapp.position.service;

import com.commuting.commutingapp.position.dto.PositionDto;

public interface PositionService {
    void add(PositionDto positionDto);

    PositionDto get(String userId);
}
