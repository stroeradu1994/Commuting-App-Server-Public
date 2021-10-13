package com.commuting.commutingapp.trip.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Match {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "match_id", updatable = false, nullable = false)
    private String id;

    @ManyToOne
    private Trip trip;

    @ManyToOne
    private TripRequest tripRequest;

    private String userId;
    private double pickupWalkingDistance;
    private double dropWalkingDistance;
    private double tripDistance;
    private String pickupPoint;
    private String dropPoint;
    private LocalDateTime leaveTime;
    private LocalDateTime arriveTime;
    private LocalDateTime pickupTime;
    private LocalDateTime dropTime;
    private boolean confirmed;
    private boolean pickedUp;
    private boolean dropped;

    public Match() {

    }

    public Match(Trip trip, TripRequest tripRequest, String userId, double tripDistance, double pickupWalkingDistance, double dropWalkingDistance, String pickupPoint, String dropPoint, LocalDateTime leaveTime, LocalDateTime arriveTime, LocalDateTime pickupTime, LocalDateTime dropTime) {
        this.userId = userId;
        this.trip = trip;
        this.tripRequest = tripRequest;
        this.pickupWalkingDistance = pickupWalkingDistance;
        this.dropWalkingDistance = dropWalkingDistance;
        this.tripDistance = tripDistance;
        this.pickupPoint = pickupPoint;
        this.dropPoint = dropPoint;
        this.leaveTime = leaveTime;
        this.arriveTime = arriveTime;
        this.pickupTime = pickupTime;
        this.dropTime = dropTime;
        this.confirmed = false;
        this.pickedUp = false;
        this.dropped = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public TripRequest getTripRequest() {
        return tripRequest;
    }

    public void setTripRequest(TripRequest tripRequest) {
        this.tripRequest = tripRequest;
    }

    public double getPickupWalkingDistance() {
        return pickupWalkingDistance;
    }

    public void setPickupWalkingDistance(double pickupWalkingDistance) {
        this.pickupWalkingDistance = pickupWalkingDistance;
    }

    public double getDropWalkingDistance() {
        return dropWalkingDistance;
    }

    public void setDropWalkingDistance(double dropWalkingDistance) {
        this.dropWalkingDistance = dropWalkingDistance;
    }

    public String getPickupPoint() {
        return pickupPoint;
    }

    public void setPickupPoint(String pickupPoint) {
        this.pickupPoint = pickupPoint;
    }

    public double getTripDistance() {
        return tripDistance;
    }

    public void setTripDistance(double tripDistance) {
        this.tripDistance = tripDistance;
    }

    public String getDropPoint() {
        return dropPoint;
    }

    public void setDropPoint(String dropPoint) {
        this.dropPoint = dropPoint;
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

    public void setArriveTime(LocalDateTime arriveTime) {
        this.arriveTime = arriveTime;
    }

    public LocalDateTime getPickupTime() {
        return pickupTime;
    }

    public LocalDateTime getDropTime() {
        return dropTime;
    }

    public void setDropTime(LocalDateTime dropTime) {
        this.dropTime = dropTime;
    }

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public boolean isPickedUp() {
        return pickedUp;
    }

    public void setPickedUp(boolean pickedUp) {
        this.pickedUp = pickedUp;
    }

    public boolean isDropped() {
        return dropped;
    }

    public void setDropped(boolean dropped) {
        this.dropped = dropped;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
