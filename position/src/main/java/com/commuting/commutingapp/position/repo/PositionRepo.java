package com.commuting.commutingapp.position.repo;

import com.commuting.commutingapp.position.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionRepo extends JpaRepository<Position, String> {
    Optional<Position> findFirstByUserIdOrderByCreatedAtDesc(String userId);
}
