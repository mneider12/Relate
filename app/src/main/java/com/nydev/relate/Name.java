package com.nydev.relate;

import android.support.annotation.NonNull;

/**
 * Created by markneider on 7/30/16.
 * Store information about a name
 */
public class Name {

    private String lastName,firstName;

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
}