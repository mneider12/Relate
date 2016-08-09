package com.nydev.relate;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.joda.time.MonthDay;

/**
 * Created by markneider on 8/8/16.
 * Functions for interacting with relationship database that are only needed during testing
 */
public class RelationshipDbTestHelper {

    /**
     * Retrieve the last relationship (highest ID) in the database.
     * This is an arbitrary choice - useful to have a relationship ot work with
     * @param context caller's context - needed to retrieve database
     * @return last relationship in the database
     */
    public static Relationship getLastRelationshipFromDatabase(Context context) {
        SQLiteOpenHelper sqLiteOpenHelper = new SQLiteOpenHelper(context,
                RelationshipDbHelper.DATABASE_NAME, null, RelationshipDbHelper.DATABASE_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {
                // don't need to do anything
            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
                // don't need to do anything
            }
        };
        SQLiteDatabase relationshipDatabase = sqLiteOpenHelper.getReadableDatabase();

        // Select last row in relationship table
        Cursor lastRelationshipCursor = relationshipDatabase.rawQuery(
                "SELECT * FROM " + RelationshipContract.RelationshipEntry.TABLE_NAME +
                        " WHERE " + RelationshipContract.RelationshipEntry._ID +
                        "= (SELECT MAX(" + RelationshipContract.RelationshipEntry._ID +
                        ")  FROM " + RelationshipContract.RelationshipEntry.TABLE_NAME + ")", null);

        // load relationship data from results
        Relationship relationship;
        if (lastRelationshipCursor.moveToFirst()) { // If a row was returned
            int relationshipId = lastRelationshipCursor.getInt(lastRelationshipCursor.getColumnIndex(
                    RelationshipContract.RelationshipEntry._ID));
            String lastName = lastRelationshipCursor.getString(lastRelationshipCursor.getColumnIndex(
                    RelationshipContract.RelationshipEntry.COLUMN_NAME_LAST_NAME));
            String firstName = lastRelationshipCursor.getString(lastRelationshipCursor.getColumnIndex(
                    RelationshipContract.RelationshipEntry.COLUMN_NAME_FIRST_NAME));
            String rawBirthday = lastRelationshipCursor.getString(lastRelationshipCursor.getColumnIndex(
                    RelationshipContract.RelationshipEntry.COLUMN_NAME_BIRTHDAY));
            relationship = new Relationship(relationshipId, lastName, firstName);
            if (rawBirthday != null) {
                MonthDay birthday = MonthDay.parse(rawBirthday);
                relationship.setBirthday(birthday);
            }
        } else { // no match found for relationshipId in _ID column
            relationship = new Relationship(); // blank relationship with ID -1. Not valid to save, no current use case.
        }

        lastRelationshipCursor.close();
        return relationship;
    }
}
