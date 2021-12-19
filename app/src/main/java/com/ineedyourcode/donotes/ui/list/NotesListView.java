package com.ineedyourcode.donotes.ui.list;

import com.ineedyourcode.donotes.domain.Note;
import com.ineedyourcode.donotes.ui.adapter.AdapterItem;

import java.util.List;

public interface NotesListView {

    void showNotes(List<AdapterItem> notes);

    void showProgress();

    void hideProgress();
}
