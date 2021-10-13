package com.commuting.commutingapp.event.repo;

import com.commuting.commutingapp.event.model.Log;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogRepo extends MongoRepository<Log, String> {
}
