package com.nydev.relate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.nydev.relate.RelationshipContract.RelationshipEntry;

/**
 * Created by markneider on 7/27/16.
 * Helper class for accessing Relationship SQLite database
 */
public class RelationshipDbHelper extends SQLiteOpenHelper {

    @SuppressWarnings("unused") // currently only used when needed for debugging
    private final String LOG_TAG = "nydev.Relate";  // Tag to use in android monitor log statements

    // If you change the database schema, you must increment the database version
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME  = "Relationship.db";

    private static final String TEXT_TYPE = " TEXT"; // define text data types for SQL columns
    private static final String DATE_TYPE = " DATE";
    private static final String COMMA_SEPARATOR = ",";

    private static final String SQL_CREATE_ENTRIES =    // SQL statement to create relationship table and define columns
            "CREATE TABLE " + RelationshipEntry.TABLE_NAME + " (" +
            RelationshipEntry._ID + " INTEGER PRIMARY KEY," +
            RelationshipEntry.COLUMN_NAME_LAST_NAME + TEXT_TYPE + COMMA_SEPARATOR +
            RelationshipEntry.COLUMN_NAME_FIRST_NAME + TEXT_TYPE + COMMA_SEPARATOR +
            RelationshipEntry.COLUMN_NAME_BIRTHDAY + DATE_TYPE + " )";

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
        relationshipDb.execSQL(SQL_CREATE_ENTRIES); // create relationship table
    }

    /**
     * To be called when a new version of the database has been created
     * @param relationshipDb database being upgraded
     * @param oldVersion old version number
     * @param newVersion new version number
     */
    public void onUpgrade(SQLiteDatabase relationshipDb, int oldVersion, int newVersion) {

    }

    /**
     * Insert Relationship info into a row of the relationship table
     * @param relationshipId unique ID of the relationship
     * @param lastName relation's last name
     * @param firstName relation's first name
     * @param birthday relation's birthday
     * @return true if the insert operation was successful, false otherwise
     */
    public boolean insertRelationship(int relationshipId, String lastName, String firstName,
                                      Date birthday) {
        SQLiteDatabase relationshipDatabase = this.getWritableDatabase();
        ContentValues relationshipValues = buildContentValues(relationshipId, lastName, firstName,
                birthday);

        // return status of insert operation
        return relationshipDatabase.insert(RelationshipEntry.TABLE_NAME, null, relationshipValues) != -1;
    }

    /**
     * Update an existing Relationship in the relationship table
     * @param relationshipId unique of an ID that already exists in the relationship table
     * @param lastName relation's last name
     * @param firstName relation's first name
     * @param birthday relation's birthday
     * @return true if the update operation was successful, false otherwise
     */
    public boolean updateRelationship(int relationshipId, String lastName, String firstName,
                                      Date birthday) {
        SQLiteDatabase relationshipDatabase = this.getWritableDatabase();
        ContentValues relationshipValues = buildContentValues(relationshipId, lastName, firstName,
                birthday);

        //return status of update operation
        return relationshipDatabase.update(RelationshipEntry.TABLE_NAME, relationshipValues,
                RelationshipEntry._ID + "=" + relationshipId, null) != -1;
    }

    /**
     * Build ContentValues from Relationship information
     * @param relationshipId unique ID identifying a row in the relationship table
     * @param lastName entry into the COLUMN_NAME_LAST_NAME column
     * @param firstName entry into the COLUMN_NAME_FIRST_NAME column
     * @param birthday entry into the COLUMN_NAME_BIRTHDAY column. Will be converted into "yyyy-MM-dd" format
     * @return ContentValues with relationship information
     */
    private ContentValues buildContentValues(int relationshipId, String lastName, String firstName,
                                             Date birthday) {
        ContentValues relationshipValues = new ContentValues();

        relationshipValues.put(RelationshipEntry._ID, relationshipId);
        relationshipValues.put(RelationshipEntry.COLUMN_NAME_LAST_NAME, lastName);
        relationshipValues.put(RelationshipEntry.COLUMN_NAME_FIRST_NAME, firstName);

        // Always use US locale for consistent read / write in database
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String birthdayFormatted = dateFormat.format(birthday);
        relationshipValues.put(RelationshipEntry.COLUMN_NAME_BIRTHDAY, birthdayFormatted);

        return relationshipValues;
    }

    /**
     * Load a relationship from the relationship table
     * @param relationshipId unique ID in the _ID column identifying the row to load
     * @return Relationship if relationshipId exists in the relationship column,
     *         otherwise a new Relationship. This Relationship will not use relationshipId, except by coincidence.
     */
    public Relationship getRelationship(int relationshipId) {
        SQLiteDatabase relationshipDatabase = this.getReadableDatabase();

        // Select all data from relationship matching relationshipId. Should always return 0 or 1 rows, because _ID is the primary key.
        Cursor relationshipCursor = relationshipDatabase.rawQuery(
                "SELECT * FROM " + RelationshipEntry.TABLE_NAME +
                " WHERE " + RelationshipEntry._ID + "=" + relationshipId,
                null);

        Relationship relationship;
        if (relationshipCursor.moveToFirst()) { // If a row was returned
            String lastName = relationshipCursor.getString(relationshipCursor.getColumnIndex(
                    RelationshipEntry.COLUMN_NAME_LAST_NAME));
            String firstName = relationshipCursor.getString(relationshipCursor.getColumnIndex(
                    RelationshipEntry.COLUMN_NAME_FIRST_NAME));
            relationship = new Relationship(relationshipId, lastName, firstName);
        }
        else { // no match found for relationshipId in _ID column
            relationship = new Relationship(); // blank relationship with ID -1. Not valid to save, no current use case.
        }

        // clean up Cursor and return
        relationshipCursor.close();
        return relationship;
    }

    /**
     * Retrieve all information from the relationship table
     * @return Cursor with one row per Relationship and all available columns
     */
    public Cursor getAllRelationships() {
        SQLiteDatabase relationshipDatabase = this.getReadableDatabase();
        return relationshipDatabase.rawQuery(
                "SELECT * FROM " + RelationshipEntry.TABLE_NAME, null);
    }

    /**
     * Check if a relationship with _ID = relationshipId is saved in the relationship table
     * @param relationshipId check for existence of this ID in the relationship table
     * @return True if relationshipId exists exactly once in the _ID column of the relationship table, otherwise false
     */
    public boolean isValidRelationshipId(int relationshipId) {
        SQLiteDatabase relationshipDatabase = this.getReadableDatabase();
        Cursor relationshipCursor = relationshipDatabase.rawQuery(
                "SELECT _ID FROM " + RelationshipEntry.TABLE_NAME +
                " WHERE _ID=" + relationshipId, null);

        boolean isValidId = relationshipCursor.getCount() == 1;
        relationshipCursor.close();
        return isValidId;
    }

    /**
     * Delete a row in the relationship table
     * @param relationshipId unique of a row in the relationship table to delete
     * @return True if the delete operation is successful, False otherwise
     */
    public boolean deleteRelationship(int relationshipId) {
        SQLiteDatabase relationshipDatabase = this.getWritableDatabase();

        return relationshipDatabase.delete(RelationshipEntry.TABLE_NAME,
                RelationshipEntry._ID + "=" + relationshipId, null) != -1;
    }
}
