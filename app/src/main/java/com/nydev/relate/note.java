package com.nydev.relate;

import android.content.Context;

import org.joda.time.LocalDate;

/**
 * Created by markneider on 8/9/16.
 * Hold a note about a relationship
 */
public class Note {
    private int noteId; // unique id for this note
    private int relationshipId; // id of related relationship to this note
    private LocalDate createdDate;
    private LocalDate noteDate;
    private String noteText;

    /**
     * Create a new shell note. This holds a reference to the related relationship,
     * but does not have a reserved noteId. noteId will equal -1.
     *
     * @param relationshipId id of related relationship to this note
     */
    public Note(int relationshipId) {
        noteId = -1; // -1 indicates that the note does not have a reserved Id.
        // The information in this note will need to be used to construct a permanent note if it is to be saved.
        this.relationshipId = relationshipId;
        this.createdDate = new LocalDate(); // today
        this. noteDate = new LocalDate(); // default to today
        this.noteText = "";
    }

    /**
     * Create a new note
     * @param context calling context, used to access shared preferences
     * @param relationshipId id of relationship this note is related to
     */
    public Note(Context context, int relationshipId) {
        noteId = PreferencesHelper.getNextNoteId(context); // reserve ID and increment ID counter
        this.relationshipId = relationshipId;
        this.createdDate = new LocalDate(); // created date is today
        this.noteDate = new LocalDate(); // contact date defaults to today until changed
        this.noteText = ""; // start with no text
    }

    /**
     * Create a note with a reserved note Id associated with a particular relationship,
     * but with no other saved information
     *
     * @param noteId reserved note ID
     * @param relationshipId related relationship ID
     */
    public Note(int noteId, int relationshipId) {
        this(noteId, relationshipId, new LocalDate(), new LocalDate(), "");
    }

    /**
     * Create a Note object for an existing note
     * @param noteId unique id for the note
     * @param relationshipId id of relationship related to this note
     * @param createdDate date the note was created
     * @param noteDate date the note is about
     * @param noteText note text
     */
    public Note(int noteId, int relationshipId, LocalDate createdDate, LocalDate noteDate,
                String noteText) {
        this.noteId = noteId;
        this.relationshipId = relationshipId;
        this.createdDate = createdDate;
        this.noteDate = noteDate;
        this.noteText = noteText;
    }

    /**
     * return the unique id for this note
     * @return unique id of note
     */
    public int getNoteId() {
        return noteId;
    }

    /**
     * return the id of the relationship related to this note
     * @return id of relationship related to this note
     */
    public int getRelationshipId() {
        return relationshipId;
    }

    /**
     * Return the date this note was created
     * @return date this note was created
     */
    public LocalDate getCreatedDate() {
        return createdDate;
    }

    /**
     * Return the date this note refers to.
     * @return date this note refers to
     */
    public LocalDate getNoteDate() {
        return noteDate;
    }

    /**
     * Return the text of this note
     * @return text of this note
     */
    public String getNoteText() {
        return noteText;
    }

    /**
     * Set this note's text
     * @param noteText text to set to this note
     */
    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    /**
     * Set the date of reference for this note
     *
     * @param noteDate date this note refers to
     */
    public void setNoteDate(LocalDate noteDate) {
        this.noteDate = noteDate;
    }
}
