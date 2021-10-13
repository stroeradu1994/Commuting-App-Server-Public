package com.commuting.commutingapp.direction.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class RouteStep {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "route_step_id", updatable = false, nullable = false)
    private String id;
    private double startLat;
    private double startLng;
    private double endLat;
    private double endLng;
    private double duration;
    private double distance;
    private int position;
    private String polyline;

    @ManyToOne
    private Route route;

    public RouteStep() {}

    public RouteStep(String id, double startLat, double startLng, double endLat, double endLng, double duration, double distance, int position, String polyline) {
        this.id = id;
        this.startLat = startLat;
        this.startLng = startLng;
        this.endLat = endLat;
        this.endLng = endLng;
        this.duration = duration;
        this.distance = distance;
        this.position = position;
        this.polyline = polyline;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    public double getStartLng() {
        return startLng;
    }

    public void setStartLng(double startLng) {
        this.startLng = startLng;
    }

    public double getEndLat() {
        return endLat;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public double getEndLng() {
        return endLng;
    }

    public void setEndLng(double endLng) {
        this.endLng = endLng;
    }

    public String getPolyline() {
        return polyline;
    }

    public void setPolyline(String polyline) {
        this.polyline = polyline;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
