package com.mdbank.util;

public class PositionUtil {

    public static int transformLatToIndex(double latitude) {
        if (latitude < -90.01 || latitude > 90.01) {
            throw new IllegalArgumentException(String.format("Latitude %s out of range (-90..90)", latitude));
        }
        return (int) Math.round((latitude + 90) * 2);
    }

    public static int transformLonToIndex(double longitude) {
        if (longitude < -180.01 || longitude >= 180) {
            throw new IllegalArgumentException(String.format("Longitude %s out of range (-180..180)", longitude));
        }
        return (int) Math.round((longitude + 180) / 0.625);
    }
}
