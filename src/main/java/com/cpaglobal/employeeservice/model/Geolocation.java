package com.cpaglobal.employeeservice.model;
import lombok.*;
import java.util.*;

import static org.apache.lucene.util.SloppyMath.haversinMeters;

@Data
@NoArgsConstructor
public class Geolocation {

    private double lat;
    private double lng;

    @Builder
    public Geolocation(double lat,double log) {
        this.lat = lat;
        this.lng = log;
    }


    private double getDistance(Geolocation firstPoint, Geolocation secondPoint) {
        return haversinMeters(firstPoint.getLat(), firstPoint.getLng(), secondPoint.getLat(), secondPoint.getLng());
    }

    public List<Geolocation> getNearestTwoPoints(final List<Geolocation> otherPoints) {

        double firstMin = Double.MAX_VALUE;
        double secMin = Double.MAX_VALUE;
        Geolocation firstNearestPoint = null;
        Geolocation secondNearestPoint = null;
        if (otherPoints.contains(this))
            otherPoints.remove(this);

        for (Geolocation location : otherPoints) {
            if (getDistance(this, location) < firstMin) {
                secMin = firstMin;
                firstMin = getDistance(this, location);
                firstNearestPoint = location;
            } else if (getDistance(this, location) < secMin) {
                secondNearestPoint = location;
                secMin = getDistance(this, location);
            }
        }
        return Arrays.asList(firstNearestPoint, secondNearestPoint);
    }


}
