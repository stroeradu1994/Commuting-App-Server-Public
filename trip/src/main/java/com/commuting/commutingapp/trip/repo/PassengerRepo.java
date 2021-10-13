package com.commuting.commutingapp.trip.repo;

import com.commuting.commutingapp.trip.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepo extends JpaRepository<Passenger, String> {
}
