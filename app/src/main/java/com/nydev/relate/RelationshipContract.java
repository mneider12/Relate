package com.nydev.relate;

import android.provider.BaseColumns;

/**
 * Created by markneider on 7/8/16.
 */
public class RelationshipContract {

    private RelationshipContract()
    {

    }

    public static abstract class RelationshipEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "relationship";
        public static final String COLUMN_NAME_RELATIONSHIP_ID = "relationshipId";
        public static final String COLUMN_NAME_FIRST_NAME = "firstName";
        public static final String COLUMN_NAME_LAST_NAME = "lastName";
    }

}
