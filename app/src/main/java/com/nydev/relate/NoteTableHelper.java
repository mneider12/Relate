package com.nydev.relate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nydev.relate.NoteContract.NoteEntry;
import com.nydev.relate.RelationshipContract.RelationshipEntry;

import org.joda.time.LocalDate;

import java.util.ArrayList;

/**
 * Created by markneider on 8/9/16.
 * Helper class for accessing the note table
 */
public class NoteTableHelper {

    private RelationshipDbHelper relationshipDbHelper;

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
     * Create a new NoteTableHelper
     * @param context Context from the calling activity, used to retrieve the database
     */
    public NoteTableHelper(Context context) {
        relationshipDbHelper = new RelationshipDbHelper(context);
    }

    public boolean insertNote(Note note) {
        SQLiteDatabase relationshipDatabase = relationshipDbHelper.getWritableDatabase();
        ContentValues noteValues = buildContentValues(note);

        return relationshipDatabase.insert(NoteEntry.TABLE_NAME, null, noteValues) != -1;
    }

    public boolean updateNote(Note note) {
        SQLiteDatabase relationshipDatabase = relationshipDbHelper.getWritableDatabase();
        ContentValues noteValues = buildContentValues(note);

        return relationshipDatabase.update(NoteEntry.TABLE_NAME, noteValues,
                NoteEntry._ID + "=" + note.getNoteId(), null) != 1;
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

    public ArrayList<Note> getNotes(Relationship relationship) {
        SQLiteDatabase relationshipDatabase = relationshipDbHelper.getReadableDatabase();

        // Select all data from relationship matching relationshipId. Should always return 0 or 1 rows, because _ID is the primary key.
        Cursor noteCursor = relationshipDatabase.rawQuery(
                "SELECT * FROM " + NoteEntry.TABLE_NAME +
                " WHERE " + NoteEntry.COLUMN_NAME_RELATIONSHIP_ID + "=" + relationship.getRelationshipId(),
                null);

        ArrayList<Note> notes = new ArrayList<>();
        if (noteCursor.moveToFirst()) {
            int noteId = noteCursor.getInt(noteCursor.getColumnIndex(
                    NoteEntry._ID));
            String createdDateRaw = noteCursor.getString(noteCursor.getColumnIndex(
                    NoteEntry.COLUMN_NAME_CREATED_DATE));
            LocalDate createdDate = LocalDate.parse(createdDateRaw);
            String contactDateRaw = noteCursor.getString(noteCursor.getColumnIndex(
                    NoteEntry.COLUMN_NAME_CONTACT_DATE));
            LocalDate contactDate = LocalDate.parse(contactDateRaw);
            String noteText = noteCursor.getString(noteCursor.getColumnIndex(
                    NoteEntry.COLUMN_NAME_NOTE_TEXT));
            Note note = new Note(noteId, relationship, createdDate, contactDate, noteText);
            notes.add(note);
        }
        return notes;
    }

    public boolean isValidNoteId(int noteId) {
        SQLiteDatabase relationshipDatabase = relationshipDbHelper.getReadableDatabase();
        Cursor relationshipCursor = relationshipDatabase.rawQuery(
                "SELECT _ID FROM " + NoteEntry.TABLE_NAME +
                        " WHERE _ID=" + noteId, null);

        boolean isValidId = relationshipCursor.getCount() == 1;
        relationshipCursor.close();
        return isValidId;
    }
}

