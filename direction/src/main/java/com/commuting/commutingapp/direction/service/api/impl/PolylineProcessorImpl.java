package com.commuting.commutingapp.direction.service.api.impl;

import com.commuting.commutingapp.common.dto.Point;
import com.commuting.commutingapp.common.utils.LocationUtils;
import com.commuting.commutingapp.common.dto.PointWithTimeDeltaAndDistanceDelta;
import com.commuting.commutingapp.direction.model.Route;
import com.commuting.commutingapp.direction.model.RouteStep;
import com.commuting.commutingapp.direction.service.api.PolylineProcessor;
import com.google.maps.internal.PolylineEncoding;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PolylineProcessorImpl implements PolylineProcessor {

    private static double MINIMUM_DISTANCE = 0.005;

    public LinkedHashSet<PointWithTimeDeltaAndDistanceDelta> processPolylines(Route route) {

        LinkedHashSet<PointWithTimeDeltaAndDistanceDelta> result = new LinkedHashSet<>();

        double totalTimeDelta = 0;
        double totalDistanceDelta = 0;

        LinkedHashSet<RouteStep> sortedSteps = getSortedRouteSteps(route);

        for (RouteStep step : sortedSteps) {

            LinkedHashSet<Point> splittedPoints = splitPolylineInPoints(step.getPolyline());
            LinkedHashSet<PointWithTimeDeltaAndDistanceDelta> splittedPointsWithDeltas = buildDefaultPointWithDeltas(splittedPoints);

            double timeDeltaForPoint = getDurationInRelationToTrafficAndMedianDurations(route, step);
            double distanceDeltaForPoint = step.getDistance();

            double timeDeltaForSplittedPoint = totalTimeDelta;
            double distanceDeltaForSplittedPoint = totalDistanceDelta;

            double timeDeltaInterval = timeDeltaForPoint / splittedPointsWithDeltas.size();
            double distanceDeltaInterval = distanceDeltaForPoint / splittedPointsWithDeltas.size();

            for (PointWithTimeDeltaAndDistanceDelta splittedPointWithDelta : splittedPointsWithDeltas) {
                splittedPointWithDelta.setTimeDelta(timeDeltaForSplittedPoint + timeDeltaInterval);
                timeDeltaForSplittedPoint = timeDeltaForSplittedPoint + timeDeltaInterval;

                splittedPointWithDelta.setDistanceDelta(distanceDeltaForSplittedPoint + distanceDeltaInterval);
                distanceDeltaForSplittedPoint = distanceDeltaForSplittedPoint + distanceDeltaInterval;
            }

            totalTimeDelta = totalTimeDelta + timeDeltaForPoint;
            totalDistanceDelta = totalDistanceDelta + distanceDeltaForPoint;

            result.addAll(splittedPointsWithDeltas);
        }
        return result;
    }

    private LinkedHashSet<PointWithTimeDeltaAndDistanceDelta> buildDefaultPointWithDeltas(LinkedHashSet<Point> splittedPoints) {
        return splittedPoints.stream().map(splittedPoint -> new PointWithTimeDeltaAndDistanceDelta(splittedPoint, 0, 0)).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private LinkedHashSet<RouteStep> getSortedRouteSteps(Route route) {
        return route.getPoints()
                .stream()
                .sorted(Comparator.comparingInt(RouteStep::getPosition))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public LinkedHashSet<Point> splitPolylineInPoints(String polyline) {
        List<Point> polylines = getPointsFromPolyline(polyline);

        LinkedHashSet<Point> allPoints = new LinkedHashSet<>();

        for (int i = 1; i < polylines.size(); i++) {
            Point first = polylines.get(i - 1);
            Point second = polylines.get(i);
            allPoints.add(first);
            allPoints.add(second);

            double distance = LocationUtils.distance(first, second);

            if (distance >= 2 * MINIMUM_DISTANCE) {
                allPoints.addAll(LocationUtils.splitLine(first, second, (int) (distance / MINIMUM_DISTANCE)));
            }
        }

        return allPoints;
    }

    private List<Point> getPointsFromPolyline(String polyline) {
        return PolylineEncoding.decode(polyline)
                .stream()
                .map(latLng -> new Point(latLng.lat, latLng.lng))
                .collect(Collectors.toList());
    }

    double getDurationInRelationToTrafficAndMedianDurations(Route route, RouteStep step) {
        return step.getDuration() * route.getDuration() / route.getMedianDuration();
    }
}
