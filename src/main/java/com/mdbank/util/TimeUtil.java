package com.mdbank.util;

import java.time.Year;

public abstract class TimeUtil {
    public static int hoursInYear(Year year) {
        return year.length()*24;
    }
}
