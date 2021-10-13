package com.commuting.commutingapp.event.repo;

import com.commuting.commutingapp.event.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepo extends MongoRepository<Event, String> {
}
