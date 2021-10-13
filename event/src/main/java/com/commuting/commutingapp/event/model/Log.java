package com.commuting.commutingapp.event.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Log {

    @Id
    private String id;

    private String log;

    private String createdAt;

    private String logger;

    private String thread;

    private String level;

    public Log() {
    }

    public Log(String log, String createdAt, String logger, String thread, String level) {
        this.log = log;
        this.createdAt = createdAt;
        this.logger = logger;
        this.thread = thread;
        this.level = level;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLogger() {
        return logger;
    }

    public void setLogger(String logger) {
        this.logger = logger;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }
}
