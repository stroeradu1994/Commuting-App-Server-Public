package com.commuting.commutingapp.profile.service.impl;

import com.commuting.commutingapp.common.utils.SecurityUtils;
import com.commuting.commutingapp.profile.dto.request.CreateCarRequest;
import com.commuting.commutingapp.profile.dto.response.CarResponse;
import com.commuting.commutingapp.profile.mapper.CarMapper;
import com.commuting.commutingapp.profile.model.Car;
import com.commuting.commutingapp.profile.model.Profile;
import com.commuting.commutingapp.profile.repo.CarRepo;
import com.commuting.commutingapp.profile.repo.ProfileRepo;
import com.commuting.commutingapp.profile.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    CarRepo carRepo;

    @Autowired
    ProfileRepo profileRepo;

    @Override
    public CarResponse createCar(CreateCarRequest createCarRequest) {
        String userId = SecurityUtils.getUserId();
        Profile profile = profileRepo.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile " + userId + " not found!"));
        Car car = new Car(
                createCarRequest.getBrand(), createCarRequest.getModel(), createCarRequest.getColor(), createCarRequest.getPlate(), profile
        );
        return CarMapper.buildCarResponse(carRepo.save(car));
    }

    @Override
    public List<CarResponse> getCarsForProfileId(String id) {
        return carRepo.findByOwnerId(id).stream().map(CarMapper::buildCarResponse).collect(Collectors.toList());
    }

    @Override
    public List<CarResponse> getCars() {
        String userId = SecurityUtils.getUserId();
        return getCarsForProfileId(userId);
    }

    @Override
    public CarResponse getCar(String id) {
        return CarMapper.buildCarResponse(carRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car " + id + " not found!")));
    }

    @Override
    public void deleteCar(String id) {
        carRepo.deleteById(id);
    }
}
