package com.commuting.commutingapp.profile.mapper;

import com.commuting.commutingapp.profile.dto.response.CarResponse;
import com.commuting.commutingapp.profile.model.Car;

public class CarMapper {

    public static CarResponse buildCarResponse(Car car) {
        return new CarResponse(
                car.getId(),
                car.getBrand(),
                car.getModel(),
                car.getColor(),
                car.getPlate()
        );
    }
}
