package com.commuting.commutingapp.common.utils;


import com.commuting.commutingapp.common.dto.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LocationUtils {

    private static double m = (1 / ((2 * Math.PI / 360) * 6378.137)) / 1000;

    private static double earthRadius = 6371000.0;

    public static String locationToString(Point point) {
        return point.getLat() + "," + point.getLng();
    }

    public static String locationToString(double lat, double lng) {
        return lat + "," + lng;
    }

    public static Point locationFromString(String point) {
        List<Double> latlng = Arrays.stream(point.split(",")).map(e -> Double.valueOf(e.trim())).collect(Collectors.toList());
        return new Point(latlng.get(0), latlng.get(1));
    }

    public static List<Point> splitLine(Point point1, Point point2, int numberOfIntermediaryPoints) {

        double lat1 = point1.getLat();
        double lat2 = point2.getLat();
        double lng1 = point1.getLng();
        double lng2 = point2.getLng();

        double d = Math.sqrt((lat1 - lat2) * (lat1 - lat2) + (lng1 - lng2) * (lng1 - lng2)) / numberOfIntermediaryPoints;
        double fi = Math.atan2(lng2 - lng1, lat2 - lat1);

        List<Point> points = new ArrayList<>();

        for (int i = 0; i <= numberOfIntermediaryPoints; ++i)
            points.add(new Point(lat1 + i * d * Math.cos(fi), lng1 + i * d * Math.sin(fi)));

        return points;
    }

    public static double distance(Point point1, Point point2) {

        double lat1 = point1.getLat();
        double lat2 = point2.getLat();
        double lng1 = point1.getLng();
        double lng2 = point2.getLng();

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lngDistance = Math.toRadians(lng2 - lng1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c * 1000;
    }

    public static Point moveLatLngToRightUp(Point point, int meters) {
        double newLatitude = point.getLat() + (meters / earthRadius) * (180 / Math.PI);
        double newLongitude = point.getLng() + (meters / earthRadius) * (180 / Math.PI) / Math.cos(newLatitude * Math.PI / 180);

        return new Point(newLatitude, newLongitude);
    }

    public static Point moveLatLngToLeftDown(Point point, int meters) {
        double newLatitude = point.getLat() - (meters / earthRadius) * (180 / Math.PI);
        double newLongitude = point.getLng() - (meters / earthRadius) * (180 / Math.PI) / Math.cos(newLatitude * Math.PI / 180);

        return new Point(newLatitude, newLongitude);
    }

}
