package com.nydev.relate;

import android.support.annotation.NonNull;

/**
 * Created by markneider on 7/30/16.
 * Store information about a name
 */
public class Name {

    private String lastName,firstName;

    public Name() {
        lastName = null;
        firstName = null;
    }

    /**
     * Parse a single String into a Name object
     * Strips white space around rawName.
     * Splits last name from first name based on the last space with non-whitespace charachters following it.
     * E.g. - " myrk von Nyder " splits into "Nyder" for lastName and "myrk von" for first name.
     * @param rawName user entered name String
     */
    public Name(String rawName) {
        rawName = rawName.trim();
        int lastSpaceIndex = rawName.lastIndexOf(' ');
        if (lastSpaceIndex == -1) { // no last name given (first name may be "")
            lastName = null;
            firstName = rawName;
        } else { // first and last name given
            lastName = rawName.substring(lastSpaceIndex + 1); // start after the last space
            firstName = rawName.substring(0, lastSpaceIndex); // end before the last space
        }
    }

    /**
     * Create a name
     * @param lastName last name
     * @param firstName first name
     */
    public Name(String lastName, String firstName) {
        this.lastName = lastName;
        this.firstName = firstName;
    }

    /**
     * return lastName
     * @return lastName or "" if lastName is null
     */
    @NonNull
    public String getLastName() {
        if (lastName == null) {
            return "";
        } else {
            return lastName;
        }
    }

    /**
     * return firstName
     * @return firstName or "" if firstName is null
     */
    @NonNull
    public String getFirstName() {
        if (firstName == null) {
            return "";
        } else {
            return firstName;
        }
    }

    /**
     * Return name in firstName lastName format
     * @return name in firstName lastName format
     */
    public String toString() {
        return getFirstName() + " " + getLastName();
    }
}
