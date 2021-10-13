package com.commuting.commutingapp.direction.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Route {

    @Id
    private String id;

    private double distance;

    private double duration;

    private double medianDuration;

    @Column(columnDefinition="TEXT")
    private String path;

    private String ne;

    private String sw;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RouteStep> points = new HashSet<>();

    public Route() {}

    public Route(String id, double distance, double duration, double medianDuration, String ne, String sw, String path, Set<RouteStep> points) {
        this.id = id;
        this.distance = distance;
        this.duration = duration;
        this.medianDuration = medianDuration;
        this.ne = ne;
        this.sw = sw;
        this.path = path;
        this.points = points;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public double getMedianDuration() {
        return medianDuration;
    }

    public void setMedianDuration(double medianDuration) {
        this.medianDuration = medianDuration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNe() {
        return ne;
    }

    public void setNe(String ne) {
        this.ne = ne;
    }

    public String getSw() {
        return sw;
    }

    public void setSw(String sw) {
        this.sw = sw;
    }

    public Set<RouteStep> getPoints() {
        return points;
    }

    public void addPoints(Set<RouteStep> points) {
        this.points.addAll(points);
    }
}
