package com.commuting.commutingapp.profile.mapper;

import com.commuting.commutingapp.profile.dto.response.ProfileResponse;
import com.commuting.commutingapp.profile.model.Profile;

public class ProfileMapper {

    public static ProfileResponse buildResponse(Profile profile) {
        return new ProfileResponse(
                profile.getId(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getEmail(),
                profile.getPhone(),
                profile.getImageUrl(),
                profile.getRating(),
                profile.getPoints()
        );
    }
}
