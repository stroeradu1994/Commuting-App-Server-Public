package com.commuting.commutingapp.trip.service.api.impl;

import com.commuting.commutingapp.common.utils.SecurityUtils;
import com.commuting.commutingapp.event.model.Event;
import com.commuting.commutingapp.event.model.EventType;
import com.commuting.commutingapp.event.service.EventService;
import com.commuting.commutingapp.location.dto.location.internal.LocationPair;
import com.commuting.commutingapp.location.service.api.LocationService;
import com.commuting.commutingapp.trip.dto.trip.common.TripRequestResponseWithMatches;
import com.commuting.commutingapp.trip.dto.triprequest.CreateTripRequestDto;
import com.commuting.commutingapp.trip.dto.triprequest.TripRequestResponse;
import com.commuting.commutingapp.trip.model.Bound;
import com.commuting.commutingapp.trip.model.Trip;
import com.commuting.commutingapp.trip.model.TripRequest;
import com.commuting.commutingapp.trip.repo.TripRequestRepo;
import com.commuting.commutingapp.trip.service.api.TripRequestService;
import com.commuting.commutingapp.trip.service.internal.BoundService;
import com.commuting.commutingapp.trip.service.internal.MatchProcessor;
import com.commuting.commutingapp.trip.service.mapper.TripRequestMapper;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripRequestServiceImpl implements TripRequestService {

    @Autowired
    MatchProcessor matchProcessor;

    @Autowired
    LocationService locationService;

    @Autowired
    TripRequestRepo tripRequestRepo;

    @Autowired
    BoundService boundService;

    @Autowired
    TripRequestMapper tripRequestMapper;

    @Autowired
    EventService eventService;

    @Override
    public String create(CreateTripRequestDto createTripRequestDto) {
        LocationPair locationPair = locationService.getLocationPair(createTripRequestDto.getFrom(), createTripRequestDto.getTo());
        TripRequest tripRequest = tripRequestMapper.buildTripRequestForCreate(createTripRequestDto, locationPair);
        TripRequest savedTripRequest = tripRequestRepo.save(tripRequest);

        Bound savedBound = boundService.saveBound(savedTripRequest, Sets.newHashSet(locationPair.getFrom().getPoint(), locationPair.getTo().getPoint()));

        savedTripRequest.setBound(savedBound);
        savedTripRequest = tripRequestRepo.save(savedTripRequest);

        matchProcessor.generateMatchesForTripRequest(savedTripRequest);

        eventService.logEvent(new Event(SecurityUtils.getUserId(), EventType.CREATE_TRIP_REQUEST));

        return savedTripRequest.getId();
    }

    @Override
    @Transactional
    public List<TripRequestResponse> get() {
        String userId = SecurityUtils.getUserId();
        return tripRequestRepo.findAllByUserIdAndConfirmed(userId, false)
                .stream()
                .map(tripRequestMapper::buildTripRequestResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TripRequestResponseWithMatches get(String id) {
        TripRequest tripRequest = tripRequestRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip Request " + id + " not found!"));
        if (tripRequest.getUserId().equals(SecurityUtils.getUserId())) {
            return tripRequestMapper.buildTripRequestResponseWithMatches(tripRequest);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No permission to view this request");
        }
    }

    @Override
    @Transactional
    public void delete(String id) {
        tripRequestRepo.deleteById(id);
    }
}
