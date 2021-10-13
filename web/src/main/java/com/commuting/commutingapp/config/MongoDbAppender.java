package com.commuting.commutingapp.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.commuting.commutingapp.common.utils.DateTimeUtils;
import com.commuting.commutingapp.event.model.Log;
import com.commuting.commutingapp.event.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Component
public class MongoDbAppender extends UnsynchronizedAppenderBase<ILoggingEvent> implements SmartLifecycle {

    @Autowired
    LogService logService;

    @Override
    protected void append(ILoggingEvent e) {
        logService.log(new Log(
                e.getFormattedMessage(),
                DateTimeUtils.getTimeNow().toString(),
                e.getThreadName(),
                e.getLoggerName(),
                e.getLevel().toString()
        ));
    }

    @Override
    public boolean isRunning() {
        return isStarted();
    }
}