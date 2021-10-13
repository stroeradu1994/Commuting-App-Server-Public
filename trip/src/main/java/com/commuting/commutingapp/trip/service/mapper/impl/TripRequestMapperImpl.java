package com.commuting.commutingapp.trip.service.mapper.impl;

import com.commuting.commutingapp.common.utils.DateTimeUtils;
import com.commuting.commutingapp.common.utils.SecurityUtils;
import com.commuting.commutingapp.location.dto.location.internal.LocationPair;
import com.commuting.commutingapp.location.mapper.LocationMapper;
import com.commuting.commutingapp.trip.dto.triprequest.CreateTripRequestDto;
import com.commuting.commutingapp.trip.dto.trip.common.TripRequestResponseWithMatches;
import com.commuting.commutingapp.trip.dto.triprequest.TripRequestResponse;
import com.commuting.commutingapp.trip.model.TripRequest;
import com.commuting.commutingapp.trip.service.mapper.MatchMapper;
import com.commuting.commutingapp.trip.service.mapper.TripRequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;

@Service
public class TripRequestMapperImpl implements TripRequestMapper {

    @Autowired
    MatchMapper matchMapper;

    public TripRequest buildTripRequestForCreate(CreateTripRequestDto createTripRequestDto, LocationPair locationPair) {
        TripRequest trip = new TripRequest();
        trip.setFrom(locationPair.getFrom());
        trip.setTo(locationPair.getTo());
        if (createTripRequestDto.isAsap()) {
            trip.setArriveBy(DateTimeUtils.getTimeNow().plusMinutes(120));
        } else {
            trip.setArriveBy(LocalDateTime.parse(createTripRequestDto.getArriveBy()));
        }
        trip.setCreatedAt(DateTimeUtils.getTimeNow());

        trip.setUserId(SecurityUtils.getUserId());
        trip.setConfirmed(false);

        return trip;
    }

    public TripRequestResponseWithMatches buildTripRequestResponseWithMatches(TripRequest tripRequest) {
        return new TripRequestResponseWithMatches(
                tripRequest.getId(),
                LocationMapper.buildLocationResponse(tripRequest.getFrom()),
                LocationMapper.buildLocationResponse(tripRequest.getTo()),
                tripRequest.getArriveBy().toString(),
                tripRequest.getMatches().stream().map(match -> matchMapper.buildTripResponse(match)).collect(Collectors.toList())
        );
    }

    public TripRequestResponse buildTripRequestResponse(TripRequest tripRequest) {
        return new TripRequestResponse(
                tripRequest.getId(),
                LocationMapper.buildLocationResponse(tripRequest.getFrom()),
                LocationMapper.buildLocationResponse(tripRequest.getTo()),
                tripRequest.getArriveBy().toString(),
                tripRequest.getMatches().size()
        );
    }
}
