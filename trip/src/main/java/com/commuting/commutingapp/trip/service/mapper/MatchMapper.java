package com.commuting.commutingapp.trip.service.mapper;

import com.commuting.commutingapp.trip.dto.trip.common.MatchResponse;
import com.commuting.commutingapp.trip.model.Match;

public interface MatchMapper {
    MatchResponse buildTripResponse(Match match);
}
