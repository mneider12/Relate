package com.nydev.relate;

import android.os.Bundle;

import org.joda.time.LocalDate;

/**
 * Created by markneider on 8/29/16.
 */
public class NoteFragmentHelper {

    private static final String NOTE_ID_KEY = "note_id";
    private static final String RELATIONSHIP_ID_KEY = "relationship_id";
    private static final String NOTE_CREATED_DATE_KEY = "note_created_date";
    private static final String NOTE_DATE_KEY = "note_date";
    private static final String NOTE_TEXT_KEY = "note_text";

    public static Bundle saveNoteBundle(Note note) {
        Bundle noteArgs = new Bundle();

        noteArgs.putInt(NOTE_ID_KEY, note.getNoteId());
        noteArgs.putInt(RELATIONSHIP_ID_KEY, note.getRelationshipId());
        noteArgs.putString(NOTE_CREATED_DATE_KEY, note.getCreatedDate().toString());
        noteArgs.putString(NOTE_DATE_KEY, note.getNoteDate().toString());
        noteArgs.putString(NOTE_TEXT_KEY, note.getNoteText());

        return noteArgs;
    }

    public static Note loadNote(Bundle noteArgs) {
        int noteId = noteArgs.getInt(NOTE_ID_KEY);
        int relationshipId = noteArgs.getInt(RELATIONSHIP_ID_KEY);
        LocalDate noteCreatedDate = new LocalDate(noteArgs.getString(NOTE_CREATED_DATE_KEY));
        LocalDate noteContactDate = new LocalDate(noteArgs.getString(NOTE_DATE_KEY));
        String noteText = noteArgs.getString(NOTE_TEXT_KEY);

        return new Note(noteId, relationshipId, noteCreatedDate, noteContactDate, noteText);
    }
}
