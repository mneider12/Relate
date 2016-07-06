package com.nydev.relate;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by markneider on 7/2/16.
 */
public class Relationship implements Serializable
{
    int id;
    private String name;
    private String date;
    private String relationship;
    private String note;

    public Relationship(int id, String name, String date, String relationshipDescription, String note)
    {
        this.id = id;
        if (name == null)
        {
            this.name = "";
        }
        else
        {
            this.name = name;
        }
        this.date = date;
        this.relationship = relationshipDescription;
        this.note = note;
    }

    /**
     * If name is set, return name. Else return id.
     * @return name to display for this relationship
     */
    @Override
    public String toString()
    {
        if (name.equals(""))
        {
            return Integer.toString(id);
        }
        else
        {
            return name;
        }
    }

    public String getName()
    {
        return name;
    }
}
