package com.nydev.relate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nydev.relate.RelationshipContract.RelationshipEntry;

/**
 * Created by markneider on 7/27/16.
 */
public class RelationshipDbHelper extends SQLiteOpenHelper {

    private final String LOG_TAG = "nydev.Relate";  // Tag to use in android monitor log statements

    // If you change the database schema, you must increment the database version
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME  = "Relationship.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + RelationshipEntry.TABLE_NAME + " (" +
            RelationshipEntry._ID + " INTEGER PRIMARY KEY," +
            RelationshipEntry.COLUMN_NAME_LAST_NAME + TEXT_TYPE + COMMA_SEP +
            RelationshipEntry.COLUMN_NAME_FIRST_NAME + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + RelationshipEntry.TABLE_NAME;

    public RelationshipDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        Log.e(LOG_TAG, SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO
    }

    public boolean insertRelationship(int relationshipId, String lastName, String firstName) {
        SQLiteDatabase relationshipDatabase = this.getWritableDatabase();
        ContentValues relationshipValues = new ContentValues();
        relationshipValues.put(RelationshipEntry._ID, relationshipId);
        relationshipValues.put(RelationshipEntry.COLUMN_NAME_LAST_NAME, lastName);
        relationshipValues.put(RelationshipEntry.COLUMN_NAME_FIRST_NAME, firstName);

        // return status of insert operation
        return relationshipDatabase.insert(DATABASE_NAME, null, relationshipValues) != -1;
    }

    public Relationship getRelationship(int relationshipId) {
        SQLiteDatabase relationshipDatabase = this.getReadableDatabase();
        Cursor relationshipCursor = relationshipDatabase.rawQuery(
                "SELECT * FROM " + DATABASE_NAME +
                " WHERE " + RelationshipEntry._ID + "=" + relationshipId,
                null);
        if (relationshipCursor == null) {
            return null; //TODO return something more useful
        }

        String lastName = relationshipCursor.getString(relationshipCursor.getColumnIndex(
                RelationshipEntry.COLUMN_NAME_LAST_NAME));
        String firstName = relationshipCursor.getString(relationshipCursor.getColumnIndex(
                RelationshipEntry.COLUMN_NAME_FIRST_NAME));
        return new Relationship(relationshipId, firstName + " " + lastName, null, null, null);
    }

    public Cursor getAllRelationships() {
        SQLiteDatabase relationshipDatabase = this.getReadableDatabase();
        return relationshipDatabase.rawQuery(
                "SELECT * FROM " + RelationshipEntry.TABLE_NAME, null);
    }
}
