package com.commuting.commutingapp.common.service.impl;

import com.commuting.commutingapp.common.service.JsonConverterService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class JsonConverterServiceImpl implements JsonConverterService {

    ObjectMapper objectMapper;

    @PostConstruct
    void init() {
        objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @SneakyThrows
    @Override
    public String convertToJson(Object obj) {
        return objectMapper.writeValueAsString(obj);
    }

    @SneakyThrows
    @Override
    @SuppressWarnings("unchecked")
    public Object convertFromJson(String json, Class cls) {
        return objectMapper.readValue(json, cls);
    }
}
