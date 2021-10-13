package com.commuting.commutingapp.trip.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Stop {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "stop_id", updatable = false, nullable = false)
    private String id;

    private String point;

    private String address;

    private double walkingDistance;

    private boolean pickup;

    private boolean arrived;

    private String userId;

    @ManyToOne
    private Trip trip;

    private LocalDateTime time;

    private boolean confirmed;

    public Stop() {}

    public Stop(String point, String address, double walkingDistance, boolean pickup, String userId, Trip trip, LocalDateTime time) {
        this.point = point;
        this.address = address;
        this.walkingDistance = walkingDistance;
        this.pickup = pickup;
        this.userId = userId;
        this.trip = trip;
        this.time = time;
        this.confirmed = false;
        this.arrived = false;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public double getWalkingDistance() {
        return walkingDistance;
    }

    public void setWalkingDistance(double walkingDistance) {
        this.walkingDistance = walkingDistance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isPickup() {
        return pickup;
    }

    public void setPickup(boolean pickup) {
        this.pickup = pickup;
    }

    public boolean isArrived() {
        return arrived;
    }

    public void setArrived(boolean arrived) {
        this.arrived = arrived;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
