package be.tomcools.geo;

import be.tomcools.domain.Location;
import be.tomcools.domain.Santa;
import be.tomcools.domain.Visit;

import java.util.HashMap;
import java.util.Map;

/**
 * Calculates the driving time (in seconds) between two locations by calculating their Haversine distance in meters
 * assuming average speed {@link #AVERAGE_SPEED_KMPH}.
 */
public final class HaversineDrivingTimeCalculator {
    private static final HaversineDrivingTimeCalculator INSTANCE = new HaversineDrivingTimeCalculator();

    public static final int SPEED_OF_LIGHT_KMPH = 300_000;
    public static final int AVERAGE_SPEED_KMPH = SPEED_OF_LIGHT_KMPH / 2; //santa moves at half of the speed of light

    private static final int EARTH_RADIUS_IN_M = 6371000;
    private static final int TWICE_EARTH_RADIUS_IN_M = 2 * EARTH_RADIUS_IN_M;

    private static final Map<String, Long> distanceMap = new HashMap<>();

    static long metersToDrivingSeconds(long meters) {
        return Math.round((double) meters / AVERAGE_SPEED_KMPH * 3.6);
    }

    public static synchronized HaversineDrivingTimeCalculator getInstance() {
        return INSTANCE;
    }

    private HaversineDrivingTimeCalculator() {
    }

    public long getTotalDrivingTimeSeconds(Santa santa) {
        var visits = santa.getVisits();
        if (visits.isEmpty()) {
            return 0;
        }

        long totalDrivingTime = 0;
        Location previousLocation = santa.getHomeLocation();

        for (Visit visit : visits) {
            totalDrivingTime += calculateDrivingTime(previousLocation,visit.location());
            previousLocation = visit.location();
        }
        totalDrivingTime += calculateDrivingTime(previousLocation, santa.getHomeLocation());

        return totalDrivingTime;
    }

    public static long calculateDrivingTime(Location from, Location to) {
        String key = "" + from.hashCode() + to.hashCode();
        if (distanceMap.containsKey(key)) {
            return distanceMap.get(key);
        } else {
            if (from.equals(to)) {
                return 0L;
            }
            CartesianCoordinate fromCartesian = locationToCartesian(from);
            CartesianCoordinate toCartesian = locationToCartesian(to);
            long drivingSeconds = metersToDrivingSeconds(calculateDistance(fromCartesian, toCartesian));
            distanceMap.put(key, drivingSeconds);
            return drivingSeconds;
        }
    }

    private static long calculateDistance(CartesianCoordinate from, CartesianCoordinate to) {
        if (from.equals(to)) {
            return 0L;
        }

        double dX = from.x - to.x;
        double dY = from.y - to.y;
        double dZ = from.z - to.z;
        double r = Math.sqrt((dX * dX) + (dY * dY) + (dZ * dZ));
        return Math.round(TWICE_EARTH_RADIUS_IN_M * Math.asin(r));
    }

    private static CartesianCoordinate locationToCartesian(Location location) {
        double latitudeInRads = Math.toRadians(location.lat());
        double longitudeInRads = Math.toRadians(location.lng());
        // Cartesian coordinates, normalized for a sphere of diameter 1.0
        double cartesianX = 0.5 * Math.cos(latitudeInRads) * Math.sin(longitudeInRads);
        double cartesianY = 0.5 * Math.cos(latitudeInRads) * Math.cos(longitudeInRads);
        double cartesianZ = 0.5 * Math.sin(latitudeInRads);
        return new CartesianCoordinate(cartesianX, cartesianY, cartesianZ);
    }

    private record CartesianCoordinate(double x, double y, double z) {

    }
}
