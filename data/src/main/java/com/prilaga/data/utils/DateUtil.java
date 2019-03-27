package com.prilaga.data.utils;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Oleg Tarashkevich on 12.05.16.
 */
public final class DateUtil {

    public static final String LONG_DATE = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String SHORT_DATE_AND_TIME = "dd.MM.yyyy HH:mm";
    public static final String SHORT_TIME_AND_DATE = "HH:mm dd.MM.yyyy";
    public static final String SHORT_DATE = "dd.MM.yyyy";
    public static final String SHORT_TIME_12 = "HH:mm a";
    public static final String SHORT_TIME_24 = "HH:mm";
    public static final String FILE_DATE = "hh_mm_ss__dd_MM_yy";

    private static SimpleDateFormatThreadSafe dateFormatLong;
    private static SimpleDateFormatThreadSafe dateFormatShort;
    private static SimpleDateFormatThreadSafe timeFormatShort12;
    private static SimpleDateFormatThreadSafe timeFormatShort24;
    private static SimpleDateFormatThreadSafe dateAndTimeFormatShort;
    private static SimpleDateFormatThreadSafe timeAndDateFormatShort;

    public static SimpleDateFormatThreadSafe dateFormatLong() {
        if (dateFormatLong == null)
            dateFormatLong = new SimpleDateFormatThreadSafe(LONG_DATE, Locale.getDefault());
        return dateFormatLong;
    }

    public static SimpleDateFormatThreadSafe dateFormatShort() {
        if (dateFormatShort == null)
            dateFormatShort = new SimpleDateFormatThreadSafe(SHORT_DATE, Locale.getDefault());
        return dateFormatShort;
    }

    public static SimpleDateFormatThreadSafe timeFormatShort12() {
        if (timeFormatShort12 == null)
            timeFormatShort12 = new SimpleDateFormatThreadSafe(SHORT_TIME_12, Locale.getDefault());
        return timeFormatShort12;
    }

    public static SimpleDateFormatThreadSafe timeFormatShort24() {
        if (timeFormatShort24 == null)
            timeFormatShort24 = new SimpleDateFormatThreadSafe(SHORT_TIME_24, Locale.getDefault());
        return timeFormatShort24;
    }

    public static SimpleDateFormatThreadSafe dateAndTimeFormatShort() {
        if (dateAndTimeFormatShort == null)
            dateAndTimeFormatShort = new SimpleDateFormatThreadSafe(SHORT_DATE_AND_TIME, Locale.getDefault());
        return dateAndTimeFormatShort;
    }

    public static SimpleDateFormatThreadSafe timeAndDateFormatShort() {
        if (timeAndDateFormatShort == null)
            timeAndDateFormatShort = new SimpleDateFormatThreadSafe(SHORT_TIME_AND_DATE, Locale.getDefault());
        return timeAndDateFormatShort;
    }

    public static Date stringToDate(String dateString, SimpleDateFormat format) {
        Date date = null;
        if (dateString != null && dateString.trim().length() > 0) {
            try {
                date = format.parse(dateString);
                return date;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    public static String dateToString(Date date, SimpleDateFormat format) {
        String datetime = null;
        if (date != null && format != null) {
            try {
                datetime = format.format(date);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return datetime;
    }

    public static String timeToString(long milliseconds) {
        long days = TimeUnit.MILLISECONDS.toDays(milliseconds);
        String timeString = days == 1 ? days + " день " : days + " дн.";
        return timeString;
    }

    public static String getDateFromLongString(String date, SimpleDateFormat format) {
        String textDate = "";
        if (!TextUtils.isEmpty(date)) {
            try {
                long time = Long.parseLong(date);
                if (time != 0) {
                    Date freshDate = new Date(time);
                    textDate = DateUtil.dateToString(freshDate, format);
                }
            } catch (Throwable throwable) {
                Log.w("DateUtil", throwable);
            }
        }
        return textDate;
    }

    public static void createEvent(Context context, long startTime, long endTime, boolean allDay, String title, String description, String location) {
//        Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime);
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, allDay);
        intent.putExtra(CalendarContract.Events.TITLE, title);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, description);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location);
        intent.putExtra(CalendarContract.Events.RRULE, "FREQ=YEARLY");

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//       intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(intent);
    }

    public static int getDaysInMonth(int month, int year) {
        switch (month) {
            case Calendar.JANUARY:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return 31;
            case 1:
                return year % 4 == 0 ? 29 : 28;
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
            default:
                throw new IllegalArgumentException("Invalid Month");
        }
    }

    public static boolean isLeapYear(Calendar calendar) {
        return calendar.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
    }

}
