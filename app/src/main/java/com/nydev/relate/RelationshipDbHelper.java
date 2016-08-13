package com.nydev.relate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nydev.relate.RelationshipContract.RelationshipEntry;
import com.nydev.relate.NoteContract.NoteEntry;

/**
 * Created by markneider on 7/27/16.
 * Helper class for accessing relationship table
 * This also contains over database methods that may be accessed by other table helpers.
 */
public class RelationshipDbHelper extends SQLiteOpenHelper {

    @SuppressWarnings("unused") // currently only used when needed for debugging
    private final String LOG_TAG = "nydev.Relate";  // Tag to use in android monitor log statements

    // If you change the database schema, you must increment the database version
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME  = "Relationship.db";

    public static final String TEXT_TYPE = " TEXT"; // define text data types for SQL columns
    public static final String DATE_TYPE = " DATE";
    public static final String COMMA_SEPARATOR = ",";
    public static final String INTEGER_TYPE = " INTEGER";

    private static final String SQL_CREATE_RELATIONSHIP_TABLE =    // SQL statement to create relationship table and define columns
            "CREATE TABLE " + RelationshipEntry.TABLE_NAME + " (" +
            RelationshipEntry._ID + " INTEGER PRIMARY KEY," +
            RelationshipEntry.COLUMN_NAME_LAST_NAME + TEXT_TYPE + COMMA_SEPARATOR +
            RelationshipEntry.COLUMN_NAME_FIRST_NAME + TEXT_TYPE + COMMA_SEPARATOR +
            RelationshipEntry.COLUMN_NAME_BIRTHDAY + DATE_TYPE + " )";

    private static final String SQL_CREATE_NOTE_TABLE = // SQL statement to create note table and define columns
            "CREATE TABLE " + NoteEntry.TABLE_NAME + " (" +
            NoteEntry._ID + " INTEGER PRIMARY KEY," +
            NoteEntry.COLUMN_NAME_RELATIONSHIP_ID + INTEGER_TYPE + COMMA_SEPARATOR +
            NoteEntry.COLUMN_NAME_CREATED_DATE + DATE_TYPE + COMMA_SEPARATOR +
            NoteEntry.COLUMN_NAME_CONTACT_DATE + DATE_TYPE + COMMA_SEPARATOR +
            NoteEntry.COLUMN_NAME_NOTE_TEXT + TEXT_TYPE + COMMA_SEPARATOR +
            "FOREIGN KEY(" + NoteEntry.COLUMN_NAME_RELATIONSHIP_ID + ") REFERENCES " +
            RelationshipEntry.TABLE_NAME + "(" + RelationshipEntry._ID + " ))";

    /**
     * Create a new RelationshipDbHelper
     * @param context Context from the calling activity, used to retrieve the database
     */
    public RelationshipDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * First time this helper is called, create the relationship table
     * @param relationshipDb the database to create the relationship table in
     */
    public void onCreate(SQLiteDatabase relationshipDb) {
        relationshipDb.execSQL(SQL_CREATE_RELATIONSHIP_TABLE); // create relationship table
        relationshipDb.execSQL(SQL_CREATE_NOTE_TABLE); // create note table
    }

    /**
     * To be called when a new version of the database has been created
     * @param relationshipDb database being upgraded
     * @param oldVersion old version number
     * @param newVersion new version number
     */
    public void onUpgrade(SQLiteDatabase relationshipDb, int oldVersion, int newVersion) {
        // not needed until the database needs to be changed after release
    }

    @Override
    public void onDowngrade(SQLiteDatabase relationshipDb, int oldVersion, int newVersion) {

    }
}
