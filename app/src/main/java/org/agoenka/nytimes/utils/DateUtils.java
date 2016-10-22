package org.agoenka.nytimes.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Author: agoenka
 * Created At: 10/22/2016
 * Version: ${VERSION}
 */

public class DateUtils {

    public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String FORMAT_MMDDYY = "MM/dd/yy";

    private DateUtils() {
        //no instance
    }

    public static Calendar getCalendar(int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return c;
    }

    public static Date getDate(String dateString, String format) {
        try {
            SimpleDateFormat sdfSource = new SimpleDateFormat(format, Locale.getDefault());
            return sdfSource.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatDate(Calendar calendar, String format) {
        if (calendar != null) {
            return formatDate(calendar.getTime(), format);
        }
        return null;
    }

    public static String formatDate(Date date, String format) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            return sdf.format(date);
        }
        return null;
    }
}
