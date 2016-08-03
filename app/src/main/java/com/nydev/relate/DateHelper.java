package com.nydev.relate;

import java.util.List;

/**
 * Created by markneider on 8/2/16.
 */
public class DateHelper {

    public enum Month {
        JANUARY (1, "Jan", 31),
        FEBRUARY (2, "Feb", 29),
        MARCH (3, "Mar", 31),
        APRIL (4, "Apr", 30),
        MAY (5, "May", 31),
        JUNE (6, "Jun", 30),
        JULY (7, "Jul", 31),
        AUGUST (8, "Aug", 31),
        SEPTEMBER (9, "Sep", 30),
        OCTOBER (10, "Oct", 31),
        NOVEMBER (11, "Nov", 30),
        DECEMBER (12, "Dec", 31);

        private final int monthOfYear;
        private final String abbreviation;
        private final int maxDays;

        Month(int monthOfYear, String abbreviation, int maxDays) {
            this.monthOfYear= monthOfYear;
            this.abbreviation = abbreviation;
            this.maxDays = maxDays;
        }

        public int getMonthOfYear() {
            return monthOfYear;
        }

        public String getAbbreviation() {
            return abbreviation;
        }

        public int getMaxDays() {
            return maxDays;
        }

        @Override
        public String toString() {
            return name().substring(0, 1) + name().substring(1).toLowerCase();
        }
    }
}
