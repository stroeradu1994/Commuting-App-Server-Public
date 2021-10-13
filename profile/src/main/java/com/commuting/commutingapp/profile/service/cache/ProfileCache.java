package com.commuting.commutingapp.profile.service.cache;

import com.commuting.commutingapp.common.wrapper.LimitedHashMap;
import com.commuting.commutingapp.profile.model.Profile;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class ProfileCache {

    private Map<String, Profile> profileCache = new LimitedHashMap<>();

    public void add(Profile profile) {
        profileCache.put(profile.getId(), profile);
    }

    public Optional<Profile> get(String id) {
        return Optional.ofNullable(profileCache.get(id));
    }
}
