package com.commuting.commutingapp.position.service.impl;

import com.commuting.commutingapp.common.utils.DateTimeUtils;
import com.commuting.commutingapp.common.utils.SecurityUtils;
import com.commuting.commutingapp.position.dto.PositionDto;
import com.commuting.commutingapp.position.model.Position;
import com.commuting.commutingapp.position.repo.PositionRepo;
import com.commuting.commutingapp.position.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class PositionServiceImpl implements PositionService {

    @Autowired
    PositionRepo positionRepo;

    @Override
    public void add(PositionDto positionDto) {
        positionRepo.save(new Position(positionDto.getLat(), positionDto.getLng(), SecurityUtils.getUserId(), DateTimeUtils.getTimeNow()));
    }

    @Override
    public PositionDto get(String userId) {
        Position position = positionRepo.findFirstByUserIdOrderByCreatedAtDesc(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Position for " + userId + " not found!"));
        return new PositionDto(position.getLat(), position.getLng());
    }
}
