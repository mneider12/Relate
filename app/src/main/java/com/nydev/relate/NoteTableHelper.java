package com.nydev.relate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nydev.relate.NoteContract.NoteEntry;

import org.joda.time.LocalDate;

import java.util.ArrayList;

/**
 * Created by markneider on 8/9/16.
 * Helper class for accessing the note table
 */
public class NoteTableHelper {

    private RelationshipDbHelper relationshipDbHelper; // Helper for accessing relationship database

    /**
     * Create a new NoteTableHelper
     * @param context Context from the calling activity, used to retrieve the database
     */
    public NoteTableHelper(Context context) {
        relationshipDbHelper = new RelationshipDbHelper(context);
    }

    /**
     * Insert a new note into the note table
     * @param note note to save
     * @return true if the insert operation was successful, otherwise false
     */
    public boolean insertNote(Note note) {
        SQLiteDatabase relationshipDatabase = relationshipDbHelper.getWritableDatabase();
        ContentValues noteValues = buildContentValues(note);

        return relationshipDatabase.insert(NoteEntry.TABLE_NAME, null, noteValues) != -1;
    }

    /**
     * Update an existing note in the database
     * @param note note with information to save.
     *             noteId must already exist in the _ID column of the note table.
     * @return true if the update operation was successful, otherwise false
     */
    public boolean updateNote(Note note) {
        SQLiteDatabase relationshipDatabase = relationshipDbHelper.getWritableDatabase();
        ContentValues noteValues = buildContentValues(note);

        return relationshipDatabase.update(NoteEntry.TABLE_NAME, noteValues,
                NoteEntry._ID + "=" + note.getNoteId(), null) != 1;
    }

    /**
     * Build the data for either an insert or update into the note table.
     * Can be used as input to insert or update to a SQLiteDatabase
     * @param note note to build data from
     * @return ContentValues representing the data for a row in the note table
     */
    private ContentValues buildContentValues(Note note) {
        ContentValues noteValues = new ContentValues();

        // build key-value pairs based on the NoteContract
        noteValues.put(NoteEntry._ID, note.getNoteId());
        noteValues.put(NoteEntry.COLUMN_NAME_RELATIONSHIP_ID, note.getRelationshipId());
        noteValues.put(NoteEntry.COLUMN_NAME_CREATED_DATE, note.getCreatedDate().toString());
        noteValues.put(NoteEntry.COLUMN_NAME_CONTACT_DATE, note.getNoteDate().toString());
        noteValues.put(NoteEntry.COLUMN_NAME_NOTE_TEXT, note.getNoteText());

        return noteValues;
    }

    /**
     * Get all of the notes for a given relationship from the note table
     * @param relationshipId id of relationship to retrieve notes for
     * @return list of notes for given relationship
     */
    public ArrayList<Note> getNotes(int relationshipId) {
        SQLiteDatabase relationshipDatabase = relationshipDbHelper.getReadableDatabase();

        // Select all data from relationship matching relationshipId. Should always return 0 or 1 rows, because _ID is the primary key.
        Cursor noteCursor = relationshipDatabase.rawQuery(
                "SELECT * FROM " + NoteEntry.TABLE_NAME +
                " WHERE " + NoteEntry.COLUMN_NAME_RELATIONSHIP_ID + "=" + relationshipId,
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
            Note note = new Note(noteId, relationshipId, createdDate, contactDate, noteText);
            notes.add(note);
        }
        noteCursor.close();
        return notes;
    }

    /**
     * Check whether noteId exists in the _ID column of the note table
     * @param noteId id of note to check for
     * @return true if noteId is a valid id in the note table, otherwise false
     */
    public boolean isValidNoteId(int noteId) {
        SQLiteDatabase relationshipDatabase = relationshipDbHelper.getReadableDatabase();
        Cursor noteCursor = relationshipDatabase.rawQuery(
                "SELECT _ID FROM " + NoteEntry.TABLE_NAME +
                        " WHERE " + NoteEntry._ID + "=" + noteId, null);

        boolean isValidId = noteCursor.getCount() == 1; // should never be more than 1 result
        noteCursor.close();
        return isValidId;
    }

    public Cursor getNoteIds(int relationshipId) {
        SQLiteDatabase relationshipDatabase = relationshipDbHelper.getReadableDatabase();
        return relationshipDatabase.rawQuery(
                "SELECT _ID FROM " + NoteEntry.TABLE_NAME +
                " WHERE " + NoteEntry.COLUMN_NAME_RELATIONSHIP_ID +"=" + relationshipId, null);
    }

    public Note getNote(int noteId) {
        SQLiteDatabase relationshipDatabase = relationshipDbHelper.getReadableDatabase();
        Cursor noteCursor = relationshipDatabase.rawQuery(
                "SELECT * FROM " + NoteEntry.TABLE_NAME +
                " WHERE " + NoteEntry._ID + "=" + noteId, null);
        noteCursor.moveToFirst();
        int relationshipId = noteCursor.getInt(noteCursor.getColumnIndex(
                NoteEntry.COLUMN_NAME_RELATIONSHIP_ID));
        String rawCreatedDate = noteCursor.getString(noteCursor.getColumnIndex(
                NoteEntry.COLUMN_NAME_CREATED_DATE));
        LocalDate createdDate = LocalDate.parse(rawCreatedDate);
        String rawContactDate = noteCursor.getString(noteCursor.getColumnIndex(
                NoteEntry.COLUMN_NAME_CONTACT_DATE));
        LocalDate contactDate = LocalDate.parse(rawContactDate);
        String noteText = noteCursor.getString(noteCursor.getColumnIndex(
                NoteEntry.COLUMN_NAME_NOTE_TEXT));
        noteCursor.close();
        return new Note(noteId, relationshipId, createdDate, contactDate, noteText);
    }

    public boolean saveNote(Note note) {
        if (isValidNoteId(note.getNoteId())) {
            return updateNote(note);
        } else {
            return insertNote(note);
        }
    }
}

