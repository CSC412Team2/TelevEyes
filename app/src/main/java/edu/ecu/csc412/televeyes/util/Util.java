package edu.ecu.csc412.televeyes.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by joshu on 11/7/2016.
 */

public class Util {

    public static Date getNextOccurenceOfDay(int dayOfWeek) {
        Calendar cal = Calendar.getInstance();
        int dow = cal.get(Calendar.DAY_OF_WEEK);
        int numDays = 7 - ((dow - dayOfWeek) % 7 + 7) % 7;
        cal.add(Calendar.DAY_OF_YEAR, numDays);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    public static int getDayFromString(String day){
        switch(day){
            case "Sunday":
                return 1;
            case "Monday":
                return 2;
            case "Tuesday":
                return 3;
            case "Wednesday":
                return 4;
            case "Thursday":
                return 5;
            case "Friday":
                return 6;
            case "Saturday":
                return 7;
            default:
                return 1;
        }
    }
}
