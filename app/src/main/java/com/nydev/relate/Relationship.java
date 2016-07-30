package com.nydev.relate;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by markneider on 7/2/16.
 * Hold information relevant to a single relationship record
 */
public class Relationship implements Serializable
{
    private int relationshipId; // unique ID. Never reused, including after deletions
    // for readability, try to always reference lastName before firstName when order doesn't otherwise matter
    private String lastName;
    private String firstName;

    /**
     * Default constructor for a relationship. Reserves an ID
     * @param context context for this activity. Needed to retrieve SharedPreferences.
     */
    public Relationship(Context context)
    {
        relationshipId = PreferencesHelper.getNextRelationshipId(context);
    }

    /**
     * Constructor for a relationship. Parameters may be null, except for relationshipId
     * @param relationshipId unique ID to use for this relationship
     * @param lastName relation's last name
     * @param firstName relation's first name
     */
    public Relationship(int relationshipId, String lastName, String firstName) {
        this.relationshipId = relationshipId;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    /**
     * String representation of the relationship
     * @return last name, first name [id]
     */
    @Override
    public String toString()
    {
        return "" + lastName + ", " + firstName + " [" + relationshipId + "]";
    }

    /**
     * Getter method for lastName
     * @return lastName or "" if lastName is null
     */
    public String getLastName() {
        return "" + lastName; // add "" to avoid returning null
    }

    /**
     * Getter method for firstName
     * @return firstName or "" if firstName is null
     */
    public String getFirstName() {
        return "" + firstName; // add "" to avoid returning null
    }

    /**
     * Get name in first last format
     * @return name in first last format
     */
    public String getName()
    {
        return "" + firstName + " " + lastName; // start with "" to avoid operating on null
    }

    /**
     * Set lastName
     * @param lastName relation's last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Set firstName
     * @param firstName relation's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter method for relationshipId
     * @return unique ID for this relationship
     */
    public int getRelationshipId()
    {
        return relationshipId;
    }
}
