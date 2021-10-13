package com.commuting.commutingapp.location.model;

import com.commuting.commutingapp.common.dto.Point;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Location {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "location_id", updatable = false, nullable = false)
    private String id;

    private String userId;

    private String label;

    private String address;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Point point;

    public Location() {
    }

    public Location(String id, String label, String address, Point point) {
        this.id = id;
        this.label = label;
        this.address = address;
        this.point = point;
    }

    public Location(String label, String address, Point point, String userId) {
        this.label = label;
        this.address = address;
        this.point = point;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }


}
