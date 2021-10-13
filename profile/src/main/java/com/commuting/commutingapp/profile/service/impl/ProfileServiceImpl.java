package com.commuting.commutingapp.profile.service.impl;

import com.commuting.commutingapp.common.utils.SecurityUtils;
import com.commuting.commutingapp.profile.dto.request.AddNameRequest;
import com.commuting.commutingapp.profile.dto.response.ProfileResponse;
import com.commuting.commutingapp.profile.mapper.ProfileMapper;
import com.commuting.commutingapp.profile.model.Profile;
import com.commuting.commutingapp.profile.repo.ProfileRepo;
import com.commuting.commutingapp.profile.service.ProfileService;
import com.commuting.commutingapp.profile.service.cache.ProfileCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    ProfileRepo profileRepo;

    @Autowired
    ProfileCache profileCache;

    @Override
    public void createUpdateProfile(Profile profile) {
        profileRepo.save(profile);
    }

    @Override
    public void addName(AddNameRequest addNameRequest) {
        Profile profile = profileRepo.findById(addNameRequest.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile " + addNameRequest.getId() + " not found!"));
        profile.setFirstName(addNameRequest.getFirstName());
        profile.setLastName(addNameRequest.getLastName());
        Profile savedProfile = profileRepo.save(profile);
        profileCache.add(savedProfile);
    }

    @Override
    public ProfileResponse getProfile() {
        String userId = SecurityUtils.getUserId();
        return ProfileMapper.buildResponse(profileCache.get(userId).orElse(profileRepo.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile " + userId + " not found!"))));
    }

    @Override
    public Profile getById(String id) {
        return profileCache.get(id).orElse(profileRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile " + id + " not found!")));
    }
}
