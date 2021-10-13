package com.commuting.commutingapp.trip.model;

import com.commuting.commutingapp.location.model.Location;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Passenger {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "passenger_id", updatable = false, nullable = false)
    private String id;

    private String userId;

    @ManyToOne
    private Trip trip;

    private LocalDateTime leaveTime;

    private LocalDateTime arriveTime;

    @ManyToOne
    private Location from;

    @ManyToOne
    private Location to;

    private double tripDistance;

    private boolean completed;

    private int rating = -1;

    public Passenger(String userId, Trip trip, LocalDateTime leaveTime, LocalDateTime arriveTime, double tripDistance, Location from, Location to) {
        this.userId = userId;
        this.trip = trip;
        this.leaveTime = leaveTime;
        this.arriveTime = arriveTime;
        this.tripDistance = tripDistance;
        this.from = from;
        this.to = to;
        this.completed = false;
    }

    public Passenger() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public LocalDateTime getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(LocalDateTime leaveTime) {
        this.leaveTime = leaveTime;
    }

    public LocalDateTime getArriveTime() {
        return arriveTime;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setArriveTime(LocalDateTime arriveTime) {
        this.arriveTime = arriveTime;
    }

    public Location getFrom() {
        return from;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Location getTo() {
        return to;
    }

    public void setTo(Location to) {
        this.to = to;
    }

    public double getTripDistance() {
        return tripDistance;
    }

    public void setTripDistance(double tripDistance) {
        this.tripDistance = tripDistance;
    }
}
