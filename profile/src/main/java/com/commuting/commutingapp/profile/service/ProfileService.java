package com.commuting.commutingapp.profile.service;


import com.commuting.commutingapp.profile.dto.request.AddNameRequest;
import com.commuting.commutingapp.profile.dto.response.ProfileResponse;
import com.commuting.commutingapp.profile.model.Profile;

public interface ProfileService {

    void createUpdateProfile(Profile profile);

    void addName(AddNameRequest addNameRequest);

    ProfileResponse getProfile();

    Profile getById(String id);
}
