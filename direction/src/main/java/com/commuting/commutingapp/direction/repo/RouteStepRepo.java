package com.commuting.commutingapp.direction.repo;

import com.commuting.commutingapp.direction.model.RouteStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteStepRepo extends JpaRepository<RouteStep, String> {

}
