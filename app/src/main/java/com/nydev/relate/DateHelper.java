package com.nydev.relate;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.Contract;

/**
 * Created by markneider on 8/2/16.
 * Helper class for dealing with Date manipulation
 */
public class DateHelper {

    /**
     * Represent a month
     */
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

        private final int monthOfYear; // E.g. 1 for January, 2 for February ...
        private final String abbreviation; // three letter abbreviation for a month
        private final int maxDays; // Maximum days that ever occur in the month (29 for February)

        /**
         * Define enum values
         * @param monthOfYear // E.g. 1 for January, 2 for February ...
         * @param abbreviation // three letter abbreviation for a month
         * @param maxDays // Maximum days that ever occur in the month (29 for February)
         */
        Month(int monthOfYear, String abbreviation, int maxDays) {
            this.monthOfYear= monthOfYear;
            this.abbreviation = abbreviation;
            this.maxDays = maxDays;
        }

        /**
         * Getter for month of year
         * @return Month of year - E.g. 1 for January, 2 for February ...
         */
        public int getMonthOfYear() {
            return monthOfYear;
        }

        /**
         * Getter for month's abbreviation
         * @return three letter abbreviation for a month
         */
        public String getAbbreviation() {
            return abbreviation;
        }

        /**
         * Get max number of days in a month
         * @return max number of days in a month (29 for February)
         */
        public int getMaxDays() {
            return maxDays;
        }

        /**
         * Lookup a Month from its monthOfYear
         * @param monthOfYear int corresponding to desired Month (e.g. 1 for January, 2 for February).
         *                    Valid inputs are between 1 and 12 inclusive.
         * @return Month corresponding to monthOfYear
         */
        @NonNull
        @Contract(pure = true) // means there are no side effects of calling this code. In this case, just return a value.
        public static Month getMonth(@IntRange(from=1, to=12) int monthOfYear) {
            switch (monthOfYear) {
                case 1: return JANUARY;
                case 2: return FEBRUARY;
                case 3: return MARCH;
                case 4: return APRIL;
                case 5: return MAY;
                case 6: return JUNE;
                case 7: return JULY;
                case 8: return AUGUST;
                case 9: return SEPTEMBER;
                case 10: return OCTOBER;
                case 11: return NOVEMBER;
                case 12: return DECEMBER;
                default: return JANUARY; // invalid input. Easier to avoid null pointer warnings if I don't return null

            }
        }

        /**
         * Return a string representation of a month
         * @return Full month name in Pascal case (e.g. January)
         */
        @Override
        public String toString() {
            return name().substring(0, 1) + name().substring(1).toLowerCase();
        }
    }
}
