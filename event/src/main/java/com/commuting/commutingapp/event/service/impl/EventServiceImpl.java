package com.commuting.commutingapp.event.service.impl;

import com.commuting.commutingapp.event.model.Event;
import com.commuting.commutingapp.event.repo.EventRepo;
import com.commuting.commutingapp.event.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    EventRepo eventRepo;

    @Override
    public void logEvent(Event event) {
        eventRepo.save(event);
    }
}
