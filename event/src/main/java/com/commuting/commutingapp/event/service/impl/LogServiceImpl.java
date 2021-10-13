package com.commuting.commutingapp.event.service.impl;

import com.commuting.commutingapp.event.model.Log;
import com.commuting.commutingapp.event.repo.EventRepo;
import com.commuting.commutingapp.event.repo.LogRepo;
import com.commuting.commutingapp.event.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    LogRepo logRepo;

    @Override
    public void log(Log log) {
        logRepo.save(log);
    }
}
