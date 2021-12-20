package com.ineedyourcode.donotes.ui.notecontent;

import com.ineedyourcode.donotes.domain.Callback;
import com.ineedyourcode.donotes.domain.Note;
import com.ineedyourcode.donotes.domain.NotesRepository;

public class AddNotePresenter {

    private AddNoteView view;
    private NotesRepository repository;

    public AddNotePresenter(AddNoteView view, NotesRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    public void saveNote(String title, String message) {
        view.showProgress();
        repository.save(title, message, new Callback<Note>() {
            @Override
            public void onSuccess(Note result) {
                view.hideProgress();
                view.noteSaved(result);
            }

            @Override
            public void onError(Throwable error) {
                view.hideProgress();
            }
        });
    }
}
