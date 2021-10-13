package com.commuting.commutingapp.trip.repo;

import com.commuting.commutingapp.trip.model.Stop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StopRepo extends JpaRepository<Stop, String> {
}
