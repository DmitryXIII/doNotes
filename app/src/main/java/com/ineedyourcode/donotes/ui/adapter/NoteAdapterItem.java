package com.ineedyourcode.donotes.ui.adapter;

import com.ineedyourcode.donotes.domain.Note;

public class NoteAdapterItem implements AdapterItem {

    private Note note;
    private String title;
    private String time;

    public NoteAdapterItem(Note note, String title, String time) {
        this.note = note;
        this.title = title;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public Note getNote() {
        return note;
    }
}
