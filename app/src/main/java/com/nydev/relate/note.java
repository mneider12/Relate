package com.nydev.relate;

import org.joda.time.LocalDate;

/**
 * Created by markneider on 8/9/16.
 * Hold a note about a relationship
 */
public class Note {
    private int noteId;
    private Relationship relationship;
    private LocalDate createdDate;
    private LocalDate contactDate;
    private String noteText;

    public Note() {
        noteId = -1;
        relationship = new Relationship();
        createdDate = new LocalDate();
        contactDate = createdDate;
        noteText = "";
    }

    public Note(Relationship relationship, LocalDate contactDate, String noteText) {
        this.relationship = relationship;
        this.createdDate = new LocalDate(); // today
        if (contactDate == null) {
            this.contactDate = new LocalDate(); // today
        } else {
            this.contactDate = contactDate;
        }
        this.noteText = noteText;
    }

    public int getNoteId() {
        return noteId;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public LocalDate getContactDate() {
        return contactDate;
    }

    public String getNoteText() {
        return noteText;
    }
}
