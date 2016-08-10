package com.nydev.relate;

import android.provider.BaseColumns;

/**
 * Created by markneider on 8/9/16.
 * Define the schema for the Note table
 */
public class NoteContract {

    /**
     * Defines constants for accessing the note table. Should not be constructed.
     */
    private NoteContract() {}

    /**
     * Define constants for note table
     */
    public static abstract class NoteEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "note";
        // _ID is the inherited id column
        public static final String COLUMN_NAME_RELATIONSHIP_ID = "relationshipId";
        public static final String COLUMN_NAME_CREATED_DATE = "createdDate";
        public static final String COLUMN_NAME_CONTACT_DATE = "contactDate";
        public static final String COLUMN_NAME_NOTE_TEXT = "noteText";
    }
}
