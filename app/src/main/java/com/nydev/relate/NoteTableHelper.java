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
     * Create a new NoteTableHelper from an existing RelationshipDbHelper
     * @param relationshipDbHelper helper for accessing relationship database
     */
    public NoteTableHelper(RelationshipDbHelper relationshipDbHelper) {
        this.relationshipDbHelper = relationshipDbHelper;
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

    public boolean deleteNote(int noteId) {
        SQLiteDatabase relationshipDatabase = relationshipDbHelper.getWritableDatabase();
        return relationshipDatabase.delete(NoteEntry.TABLE_NAME,
                NoteEntry._ID + "=" + noteId, null) == 1;
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

    /**
     * Get a cursor over the list of note IDs for a given relationship
     *
     * @param relationshipId relationship ID that note IDs relate to
     * @return Cursor over note IDs related to relationship ID
     */
    public Cursor getNoteIds(int relationshipId) {
        SQLiteDatabase relationshipDatabase = relationshipDbHelper.getReadableDatabase();
        return relationshipDatabase.rawQuery(
                "SELECT _ID FROM " + NoteEntry.TABLE_NAME +
                " WHERE " + NoteEntry.COLUMN_NAME_RELATIONSHIP_ID +"=" + relationshipId, null);
    }

    /**
     * Load a note from the database
     * Does not perform validation that noteId is valid. Do not call with an invalid noteId.
     * @param noteId ID of the note to load
     * @return saved note from the database
     */
    public Note getNote(int noteId) {
        SQLiteDatabase relationshipDatabase = relationshipDbHelper.getReadableDatabase();
        // Select everything in the row with noteId.
        Cursor noteCursor = relationshipDatabase.rawQuery(
                "SELECT * FROM " + NoteEntry.TABLE_NAME +
                " WHERE " + NoteEntry._ID + "=" + noteId, null);
        noteCursor.moveToFirst(); // should only ever be one result
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

    /**
     * Either insert or update note values in the database. If the note already exists in the
     * database, it will be updated, if not, then a new note will be inserted.
     *
     * @param note note to save
     * @return true if the save operation was successful, otherwise false.
     */
    public boolean saveNote(Note note) {
        if (isValidNoteId(note.getNoteId())) {
            return updateNote(note);
        } else {
            return insertNote(note);
        }
    }

    /**
     * Delete all notes for a given relationship
     *
     * @param relationshipId ID of relationship to delete notes for
     * @return true if all notes were deleted (including if none are found), otherwise false
     */
    public boolean deleteAllNotes(int relationshipId) {
        SQLiteDatabase relationshipDatabase = relationshipDbHelper.getWritableDatabase();
        relationshipDatabase.delete(NoteEntry.TABLE_NAME,
                NoteEntry.COLUMN_NAME_RELATIONSHIP_ID + "=" + relationshipId, null);
        return true;
    }
}

