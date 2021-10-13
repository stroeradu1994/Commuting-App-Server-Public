package com.commuting.commutingapp.trip.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class Bound {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "bound_id", updatable = false, nullable = false)
    private String id;

    private double neLat;

    private double neLng;

    private double swLat;

    private double swLng;

    @OneToOne
    private Trip trip;

    @OneToOne
    private TripRequest tripRequest;

    public Bound(double neLat, double neLng, double swLat, double swLng) {
        this.neLat = neLat;
        this.neLng = neLng;
        this.swLat = swLat;
        this.swLng = swLng;
    }

    public Bound() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getNeLat() {
        return neLat;
    }

    public void setNeLat(double neLat) {
        this.neLat = neLat;
    }

    public double getNeLng() {
        return neLng;
    }

    public void setNeLng(double neLng) {
        this.neLng = neLng;
    }

    public double getSwLat() {
        return swLat;
    }

    public void setSwLat(double swLat) {
        this.swLat = swLat;
    }

    public double getSwLng() {
        return swLng;
    }

    public void setSwLng(double swLng) {
        this.swLng = swLng;
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
}
