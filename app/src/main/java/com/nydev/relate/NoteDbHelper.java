package com.nydev.relate;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nydev.relate.NoteContract.NoteEntry;
import com.nydev.relate.RelationshipContract.RelationshipEntry;

/**
 * Created by markneider on 8/9/16.
 * Helper class for accessing the note table
 */
public class NoteDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =    // SQL statement to create relationship table and define columns
            "CREATE TABLE " + NoteEntry.TABLE_NAME + " (" +
            NoteEntry._ID + " INTEGER PRIMARY KEY," +
            NoteEntry.COLUMN_NAME_RELATIONSHIP_ID + RelationshipDbHelper.INTEGER_TYPE + RelationshipDbHelper.COMMA_SEPARATOR +
            NoteEntry.COLUMN_NAME_CREATED_DATE + RelationshipDbHelper.TEXT_TYPE + RelationshipDbHelper.COMMA_SEPARATOR +
            NoteEntry.COLUMN_NAME_CONTACT_DATE + RelationshipDbHelper.TEXT_TYPE + RelationshipDbHelper.COMMA_SEPARATOR +
            NoteEntry.COLUMN_NAME_NOTE_TEXT + RelationshipDbHelper.TEXT_TYPE +
            "FOREIGN KEY(" + NoteEntry.COLUMN_NAME_RELATIONSHIP_ID + ") REFERENCES " +
            RelationshipEntry.TABLE_NAME + "(" + RelationshipEntry._ID + " )";

    /**
     * Create a new NoteDbHelper
     * @param context Context from the calling activity, used to retrieve the database
     */
    public NoteDbHelper(Context context) {
        super(context, RelationshipDbHelper.DATABASE_NAME, null,
                RelationshipDbHelper.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase relationshipDatabase) {
        relationshipDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insertNote(Note note) {
        SQLiteDatabase relationshipDatabase = this.getWritableDatabase();
        ContentValues noteValues = buildContentValues(note);

        return relationshipDatabase.insert(NoteEntry.TABLE_NAME, null, noteValues) != -1;
    }

    /**
     * Build the data for either an insert or update into the note table.
     * Can be used as input to insert or update to a SQLiteDatabase
     * @param note relationship to build data from
     * @return ContentValues representing the data for a row in the relationship database
     */
    private ContentValues buildContentValues(Note note) {
        ContentValues noteValues = new ContentValues();

        // build key-value pairs based on the NoteContract
        noteValues.put(NoteEntry._ID, note.getNoteId());
        noteValues.put(NoteEntry.COLUMN_NAME_RELATIONSHIP_ID, note.getRelationship().getRelationshipId());
        noteValues.put(NoteEntry.COLUMN_NAME_CREATED_DATE, note.getCreatedDate().toString());
        noteValues.put(NoteEntry.COLUMN_NAME_CONTACT_DATE, note.getContactDate().toString());
        noteValues.put(NoteEntry.COLUMN_NAME_NOTE_TEXT, note.getNoteText());

        return noteValues;
    }
}

