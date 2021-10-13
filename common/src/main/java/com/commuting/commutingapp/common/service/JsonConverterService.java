package com.commuting.commutingapp.common.service;

public interface JsonConverterService {
    String convertToJson(Object obj);

    Object convertFromJson(String json, Class cls);
}
