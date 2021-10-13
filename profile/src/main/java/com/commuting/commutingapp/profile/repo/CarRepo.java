package com.commuting.commutingapp.profile.repo;

import com.commuting.commutingapp.profile.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepo extends JpaRepository<Car, String> {
    List<Car> findByOwnerId(String id);
}
