package com.nydev.relate;

import android.content.Context;

import org.joda.time.MonthDay;

/**
 * Created by markneider on 7/2/16.
 * Hold information relevant to a single relationship record
 */
public class Relationship
{
    private int relationshipId; // unique ID. Never reused, including after deletions

    private Name name;
    private MonthDay birthday;


    /**
     * Create an placeholder Relationship with ID = -1 and default / null information
     */
    public Relationship() {
        relationshipId = -1; // this is an invalid Relationship and cannot be saved until it is assigned an ID
        name = new Name();
    }

    /**
     * Default constructor for a relationship. Reserves an ID
     * @param context context for this activity. Needed to retrieve SharedPreferences.
     */
    public Relationship(Context context)
    {
        relationshipId = PreferencesHelper.getNextRelationshipId(context);
        name = new Name();
    }

    /**
     * Constructor for a relationship. Parameters may be null, except for relationshipId
     * @param relationshipId unique ID to use for this relationship
     * @param lastName relation's last name
     * @param firstName relation's first name
     */
    public Relationship(int relationshipId, String lastName, String firstName) {
        this.relationshipId = relationshipId;
        name = new Name(lastName, firstName);
    }

    /**
     * String representation of the relationship
     * @return last name, first name [id]
     */
    @Override
    public String toString()
    {
        return name.toString() + " [" + relationshipId + "]";
    }

    /**
     * Getter method for lastName
     * @return lastName or "" if lastName is null
     */
    @SuppressWarnings("unused")  // Remove this line once functionality is added using this method
    public String getLastName() {
        return name.getLastName();
    }

    /**
     * Getter method for firstName
     * @return firstName or "" if firstName is null
     */
    @SuppressWarnings("unused") // Remove this line once functionality is added using this method
    public String getFirstName() {
        return name.getFirstName();
    }

    /**
     * Get name
     * @return name
     */
    public Name getName()
    {
        return name;
    }

    /**
     * Getter method for relationshipId
     * @return unique ID for this relationship
     */
    public int getRelationshipId()
    {
        return relationshipId;
    }

    public MonthDay getBirthday() {
        return birthday;
    }

    public void setBirthday(MonthDay birthday) {
        this.birthday = birthday;
    }

    public String getBirthdayString() {

        if (birthday == null) {
            return "";
        } else {
            return birthday.toString();
        }
    }

    public void setName(Name name) {
        this.name = name;
    }
}
