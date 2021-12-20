package com.ineedyourcode.donotes.ui.list;

import com.ineedyourcode.donotes.domain.Note;
import com.ineedyourcode.donotes.ui.adapter.AdapterItem;
import com.ineedyourcode.donotes.ui.adapter.NoteAdapterItem;

import java.util.List;

public interface NotesListView {

    void showNotes(List<AdapterItem> notes);

    void showProgress();

    void hideProgress();

    void showEmpty();

    void hideEmpty();

    void onNoteAdded(NoteAdapterItem adapterItem);

    void onNoteRemoved(Note selectedNote);
}
