package com.ineedyourcode.donotes.domain;

import android.content.Context;

import com.ineedyourcode.donotes.R;

import java.util.ArrayList;
import java.util.List;

public class NotesRepositoryBuffer implements NotesRepository{


    @Override
    public List<Note> getNotes(Context context) {
        ArrayList<Note> updatedNotesList = new ArrayList<>();
        updatedNotesList.add(new Note(R.string.note_1_title, getNoteContent(R.string.note_1_content, context), R.string.note_1_create_date));
        updatedNotesList.add(new Note(R.string.note_2_title, getNoteContent(R.string.note_2_content, context), R.string.note_2_create_date));
        updatedNotesList.add(new Note(R.string.note_3_title, getNoteContent(R.string.note_3_content, context), R.string.note_3_create_date));
        updatedNotesList.add(new Note(R.string.note_4_title, getNoteContent(R.string.note_4_content, context), R.string.note_4_create_date));
        updatedNotesList.add(new Note(R.string.note_5_title, getNoteContent(R.string.note_5_content, context), R.string.note_5_create_date));

        return updatedNotesList;
    }

    private String getNoteContent (int resourceId, Context context) {
        String noteContent = "";
        String tempContent = context.getString(resourceId);
        for (int i = 0; i < 30; i++) {
            if (i < 29) {
                noteContent = noteContent + tempContent + ", ";
            } else {
                noteContent = noteContent + tempContent;
            }
        }
        return noteContent;
    }
}
