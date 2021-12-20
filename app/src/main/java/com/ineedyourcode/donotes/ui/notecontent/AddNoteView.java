package com.ineedyourcode.donotes.ui.notecontent;

import com.ineedyourcode.donotes.domain.Note;

public interface AddNoteView {
    void noteSaved (Note note);

    void showProgress();

    void hideProgress();
}
