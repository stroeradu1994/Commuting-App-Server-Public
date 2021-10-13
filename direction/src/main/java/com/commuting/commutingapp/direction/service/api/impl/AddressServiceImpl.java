package com.commuting.commutingapp.direction.service.api.impl;

import com.commuting.commutingapp.common.dto.Point;
import com.commuting.commutingapp.direction.api.AddressApi;
import com.commuting.commutingapp.direction.service.api.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressApi addressApi;

    @Override
    public String getAddress(Point point) {
        return addressApi.getAddress(point);
    }
}
