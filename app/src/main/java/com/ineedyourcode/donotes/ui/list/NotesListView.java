package com.ineedyourcode.donotes.ui.list;

import com.ineedyourcode.donotes.domain.Note;

import java.util.List;

public interface NotesListView {

    void showNotes(List<Note> notes);

    void showProgress();

    void hideProgress();
}
