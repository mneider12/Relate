package com.nydev.relate;

import android.provider.BaseColumns;

/**
 * Created by markneider on 7/8/16.
 * Define the schema for the relationship table
 */
public class RelationshipContract {

    /**
     * Defines constants for accessing relationship table. Constructor should not be called
     */
    private RelationshipContract()
    {

    }

    /**
     * Define constants for relationship table
     */
    public static abstract class RelationshipEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "relationship";
        // _ID is the inherited id column
        public static final String COLUMN_NAME_FIRST_NAME = "firstName";
        public static final String COLUMN_NAME_LAST_NAME = "lastName";
    }

}
