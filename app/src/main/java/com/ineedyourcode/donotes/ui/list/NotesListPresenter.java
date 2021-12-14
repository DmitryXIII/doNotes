package com.ineedyourcode.donotes.ui.list;

import android.content.Context;

import com.ineedyourcode.donotes.domain.Note;
import com.ineedyourcode.donotes.domain.NotesRepository;

import java.util.List;

public class NotesListPresenter {

    private NotesListView view;

    private NotesRepository repository;

    public NotesListPresenter(NotesListView view, NotesRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    public void updateNotesList (Context context) {
       List<Note> updatedNotesList = repository.getNotes(context);

        view.showNotes(updatedNotesList);
    }
}
