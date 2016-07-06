package com.nydev.relate;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by markneider on 7/2/16.
 */
public class Relationship implements Serializable
{
    int id;
    private String name;
    private Date birthday;
    private String relationship;
    private String note;

    public Relationship(int id, String name, Date birthday, String relationshipDescription, String note)
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
        this.birthday = birthday;
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

    public void setName(String name)
    {
        this.name = name;
    }

    public Date getBirthday()
    {
        return birthday;
    }

    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
    }

    public String getRelationship()
    {
        return relationship;
    }

    public String getNote()
    {
        return note;
    }
}
