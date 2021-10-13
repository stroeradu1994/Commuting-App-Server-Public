package com.commuting.commutingapp.trip.repo;

import com.commuting.commutingapp.trip.model.Bound;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoundRepo extends JpaRepository<Bound, String> {
}
