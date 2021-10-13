package com.commuting.commutingapp.trip.repo;

import com.commuting.commutingapp.trip.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchRepo extends JpaRepository<Match, String> {
    List<Match> findAllByConfirmedAndUserId(boolean confirmed, String userId);
    Optional<Match> findByTripIdAndUserId(String tripId, String userId);
}
