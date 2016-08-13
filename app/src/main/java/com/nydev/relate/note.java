package com.nydev.relate;

import android.content.Context;

import org.joda.time.LocalDate;

/**
 * Created by markneider on 8/9/16.
 * Hold a note about a relationship
 */
public class Note {
    private int noteId; // unique id for this note
    private Relationship relationship; //
    private LocalDate createdDate;
    private LocalDate contactDate;
    private String noteText;

    /**
     * Create a new note
     * @param context calling context, used to access shared preferences
     * @param relationship relationship this note is related to
     */
    public Note(Context context, Relationship relationship) {
        noteId = PreferencesHelper.getNextNoteId(context); // reserve ID and increment ID counter
        this.relationship = relationship;
        this.createdDate = new LocalDate(); // created date is today
        this.contactDate = new LocalDate(); // contact date defaults to today until changed
        this.noteText = ""; // start with no text
    }

    /**
     * Create a Note object for an existing note
     * @param noteId unique id for the note
     * @param relationship relationship related to this note
     * @param createdDate date the note was created
     * @param contactDate date the note is about
     * @param noteText note text
     */
    public Note(int noteId, Relationship relationship, LocalDate createdDate, LocalDate contactDate,
                String noteText) {
        this.noteId = noteId;
        this.relationship = relationship;
        this.createdDate = createdDate;
        this.contactDate = contactDate;
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
     * return the relationship related to this note
     * @return relationship related to this note
     */
    public Relationship getRelationship() {
        return relationship;
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
    public LocalDate getContactDate() {
        return contactDate;
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
}
