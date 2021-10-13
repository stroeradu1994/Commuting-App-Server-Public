package com.commuting.commutingapp.direction.repo;

import com.commuting.commutingapp.direction.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepo extends JpaRepository<Route, String> {

}
