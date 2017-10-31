package com.amit.cambium.utils;

import android.text.TextUtils;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Utility class for date related methods
 *
 * Created by Amit Barjatya on 10/29/17.
 */

public class DateUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZZ");

    /**
     * converting string date to java.util date format
     * @param date string representation of date
     * @return java.util date
     */
    public static Date from(String date) {
        if (TextUtils.isEmpty(date) || date.equals("null"))
            return null;
        return DATE_TIME_FORMATTER.parseDateTime(date).toDate();
    }

}
