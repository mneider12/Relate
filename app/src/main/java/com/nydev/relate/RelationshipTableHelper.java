package com.nydev.relate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nydev.relate.RelationshipContract.RelationshipEntry;

import org.joda.time.MonthDay;

/**
 * Created by markneider on 8/13/16.
 * Methods to help access the relationship table in relationship.db
 */
public class RelationshipTableHelper {

    private RelationshipDbHelper relationshipDbHelper;

    public RelationshipTableHelper(Context context) {
        relationshipDbHelper = new RelationshipDbHelper(context);
    }

    /**
     * Check if a relationship with _ID = relationshipId is saved in the relationship table
     * @param relationshipId check for existence of this ID in the relationship table
     * @return True if relationshipId exists exactly once in the _ID column of the relationship table, otherwise false
     */
    public boolean isValidRelationshipId(int relationshipId) {
        SQLiteDatabase relationshipDatabase = relationshipDbHelper.getReadableDatabase();
        Cursor relationshipCursor = relationshipDatabase.rawQuery(
                "SELECT _ID FROM " + RelationshipEntry.TABLE_NAME +
                        " WHERE _ID=" + relationshipId, null);

        boolean isValidId = relationshipCursor.getCount() == 1;
        relationshipCursor.close();
        return isValidId;
    }

    /**
     * Insert a Relationship into the relationship database
     * @param relationship relationship to save
     * @return true if the insert was successful, otherwise false
     */
    public boolean insertRelationship(Relationship relationship) {
        SQLiteDatabase relationshipDatabase = relationshipDbHelper.getWritableDatabase();
        ContentValues relationshipValues = buildContentValues(relationship); // build data for insert

        return relationshipDatabase.insert(RelationshipEntry.TABLE_NAME, null, relationshipValues) != -1;
    }

    /**
     * Update an existing relationship in the relationship database
     * @param relationship relationship to update in the database
     * @return true if the update was successful, otherwise false
     */
    public boolean updateRelationship(Relationship relationship) {
        SQLiteDatabase relationshipDatabase = relationshipDbHelper.getWritableDatabase();
        ContentValues relationshipValues = buildContentValues(relationship); // build data for update

        return relationshipDatabase.update(RelationshipEntry.TABLE_NAME, relationshipValues,
                RelationshipEntry._ID + "=" + relationship.getRelationshipId(), null) != -1;
    }

    /**
     * Build the data for either an insert or update into the relationship database.
     * Can be used as input to insert or update to a SQLiteDatabase
     * @param relationship relationship to build data from
     * @return ContentValues representing the data for a row in the relationship database
     */
    private ContentValues buildContentValues(Relationship relationship) {
        ContentValues relationshipValues = new ContentValues();

        // build key-value pairs based on the RelationshipContract
        relationshipValues.put(RelationshipEntry._ID, relationship.getRelationshipId());
        relationshipValues.put(RelationshipEntry.COLUMN_NAME_LAST_NAME, relationship.getLastName());
        relationshipValues.put(RelationshipEntry.COLUMN_NAME_FIRST_NAME, relationship.getFirstName());
        relationshipValues.put(RelationshipEntry.COLUMN_NAME_BIRTHDAY, relationship.getBirthdayString());

        return relationshipValues;
    }

    /**
     * Load a relationship from the relationship table
     * @param relationshipId unique ID in the _ID column identifying the row to load
     * @return Relationship if relationshipId exists in the relationship column,
     *         otherwise a new Relationship. This Relationship will not use relationshipId, except by coincidence.
     */
    public Relationship getRelationship(int relationshipId) {
        SQLiteDatabase relationshipDatabase = relationshipDbHelper.getReadableDatabase();

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
            String rawBirthday = relationshipCursor.getString(relationshipCursor.getColumnIndex(
                    RelationshipEntry.COLUMN_NAME_BIRTHDAY));
            relationship = new Relationship(relationshipId, lastName, firstName);
            if (!rawBirthday.equals("")) {
                MonthDay birthday = MonthDay.parse(rawBirthday);
                relationship.setBirthday(birthday);
            }
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
        SQLiteDatabase relationshipDatabase = relationshipDbHelper.getReadableDatabase();
        return relationshipDatabase.rawQuery(
                "SELECT * FROM " + RelationshipEntry.TABLE_NAME, null);
    }

    /**
     * Delete a row in the relationship table
     * @param relationshipId unique of a row in the relationship table to delete
     * @return True if the delete operation is successful, False otherwise
     */
    public boolean deleteRelationship(int relationshipId) {
        SQLiteDatabase relationshipDatabase = relationshipDbHelper.getWritableDatabase();

        return relationshipDatabase.delete(RelationshipEntry.TABLE_NAME,
                RelationshipEntry._ID + "=" + relationshipId, null) != -1;
    }
}
