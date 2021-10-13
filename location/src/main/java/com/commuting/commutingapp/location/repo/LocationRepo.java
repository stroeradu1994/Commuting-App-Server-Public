package com.commuting.commutingapp.location.repo;

import com.commuting.commutingapp.location.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepo extends JpaRepository<Location, String> {
    List<Location> findAllByUserId(String userId);
}
