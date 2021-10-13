package com.commuting.commutingapp.location.service.api.impl;

import com.commuting.commutingapp.common.dto.Point;
import com.commuting.commutingapp.common.utils.SecurityUtils;
import com.commuting.commutingapp.location.dto.location.internal.LocationPair;
import com.commuting.commutingapp.location.dto.location.request.CreateLocationRequest;
import com.commuting.commutingapp.location.dto.location.request.UpdateLocationRequest;
import com.commuting.commutingapp.location.dto.location.response.LocationResponse;
import com.commuting.commutingapp.location.mapper.LocationMapper;
import com.commuting.commutingapp.location.model.Location;
import com.commuting.commutingapp.location.repo.LocationRepo;
import com.commuting.commutingapp.location.service.api.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    LocationRepo locationRepo;

    @Override
    public LocationResponse createLocation(CreateLocationRequest createLocationRequest) {
        String userId = SecurityUtils.getUserId();
        Location location = new Location(createLocationRequest.getLabel(), createLocationRequest.getAddress(), new Point(createLocationRequest.getLat(), createLocationRequest.getLng()), userId);
        Location savedLocation = locationRepo.save(location);
        return LocationMapper.buildLocationResponse(savedLocation);
    }

    @Override
    public LocationResponse updateLocation(UpdateLocationRequest updateLocationRequest) {
        return locationRepo.findById(updateLocationRequest.getId()).map(location -> {
            Location locationToUpdate = new Location(location.getId(), updateLocationRequest.getLabel(), updateLocationRequest.getAddress(), new Point(updateLocationRequest.getLat(), updateLocationRequest.getLng()));
            Location savedLocation = locationRepo.save(locationToUpdate);
            return LocationMapper.buildLocationResponse(savedLocation);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location " + updateLocationRequest.getId() + " not found!"));
    }

    @Override
    public List<LocationResponse> getLocations() {
        String userId = SecurityUtils.getUserId();
        return locationRepo.findAllByUserId(userId).stream().map(LocationMapper::buildLocationResponse).collect(Collectors.toList());
    }

    @Override
    public Location getLocation(String id) {
        return locationRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location " + id + " not found!"));
    }

    @Override
    public LocationPair getLocationPair(String from, String to) {
        return new LocationPair(getLocation(from), getLocation(to));
    }

    @Override
    public void deleteLocation(String id) {
        locationRepo.deleteById(id);
    }

}
