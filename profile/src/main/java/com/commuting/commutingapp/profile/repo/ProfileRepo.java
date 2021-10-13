package com.commuting.commutingapp.profile.repo;


import com.commuting.commutingapp.profile.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepo extends JpaRepository<Profile, String> {

}
