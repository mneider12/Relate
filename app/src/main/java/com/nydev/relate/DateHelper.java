package com.nydev.relate;

import java.util.List;

/**
 * Created by markneider on 8/2/16.
 */
public class DateHelper {

    public enum Month {
        JANUARY (1, "Jan"),
        FEBRUARY (2, "Feb"),
        MARCH (3, "Mar"),
        APRIL (4, "Apr"),
        MAY (5, "May"),
        JUNE (6, "Jun"),
        JULY (7, "Jul"),
        AUGUST (8, "Aug"),
        SEPTEMBER (9, "Sep"),
        OCTOBER (10, "Oct"),
        NOVEMBER (11, "Nov"),
        DECEMBER (12, "Dec");

        private final int monthOfYear;
        private final String abbreviation;

        Month(int monthOfYear, String abbreviation) {
            this.monthOfYear= monthOfYear;
            this.abbreviation = abbreviation;
        }

        public int getMonthOfYear() {
            return monthOfYear;
        }

        public String getAbbreviation() {
            return abbreviation;
        }
    }
}
