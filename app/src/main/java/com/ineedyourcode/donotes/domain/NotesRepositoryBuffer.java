package com.ineedyourcode.donotes.domain;

import com.ineedyourcode.donotes.R;

import java.util.ArrayList;
import java.util.List;

public class NotesRepositoryBuffer implements NotesRepository{
    @Override
    public List<Note> getNotes() {
        ArrayList<Note> updatedNotesList = new ArrayList<>();
        updatedNotesList.add(new Note(R.string.note_1_title, R.string.note_1_content, R.string.note_1_create_date));
        updatedNotesList.add(new Note(R.string.note_2_title, R.string.note_2_content, R.string.note_2_create_date));
        updatedNotesList.add(new Note(R.string.note_3_title, R.string.note_3_content, R.string.note_3_create_date));
        updatedNotesList.add(new Note(R.string.note_4_title, R.string.note_4_content, R.string.note_4_create_date));
        updatedNotesList.add(new Note(R.string.note_5_title, R.string.note_5_content, R.string.note_5_create_date));

        return updatedNotesList;
    }
}
