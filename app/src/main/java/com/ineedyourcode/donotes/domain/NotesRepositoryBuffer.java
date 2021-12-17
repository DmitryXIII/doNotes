package com.ineedyourcode.donotes.domain;

import android.content.Context;

import com.ineedyourcode.donotes.R;

import java.util.ArrayList;
import java.util.List;

public class NotesRepositoryBuffer implements NotesRepository{


    @Override
    public List<Note> getNotes(Context context) {
        ArrayList<Note> updatedNotesList = new ArrayList<>();

        for (int i = 1; i < 101; i++) {
            updatedNotesList.add(new Note(context.getString(R.string.note_title) + "_" + i, getNoteContent(R.string.note_content, context, i), R.string.note_create_date));

        }
        return updatedNotesList;
    }

    private String getNoteContent (int resourceId, Context context, int i) {
        String noteContent = "";
        String tempContent = context.getString(resourceId);
        for (int j = 0; j < 100; j++) {
            noteContent = noteContent + tempContent + "_" + i + ", ";
            if (j < 99) {
            } else {
                noteContent = noteContent + tempContent + "_" + i;
            }
        }
        return noteContent;
    }
}
