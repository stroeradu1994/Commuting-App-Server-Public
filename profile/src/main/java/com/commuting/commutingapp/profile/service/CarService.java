package com.commuting.commutingapp.profile.service;

import com.commuting.commutingapp.profile.dto.request.CreateCarRequest;
import com.commuting.commutingapp.profile.dto.response.CarResponse;

import java.util.List;

public interface CarService {
    CarResponse createCar(CreateCarRequest createCarRequest);

    List<CarResponse> getCarsForProfileId(String id);

    List<CarResponse> getCars();

    CarResponse getCar(String id);

    void deleteCar(String id);
}
