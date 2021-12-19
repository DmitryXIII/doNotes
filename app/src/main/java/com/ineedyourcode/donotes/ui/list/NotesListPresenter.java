package com.ineedyourcode.donotes.ui.list;

import com.ineedyourcode.donotes.domain.Callback;
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

    public void requestNotes() {
        view.showProgress();

        repository.getAll(new Callback<List<Note>>() {
            @Override
            public void onSuccess(List<Note> result) {
                view.showNotes(result);
                view.hideProgress();
            }

            @Override
            public void onError(Throwable error) {
                view.hideProgress();
            }
        });

    }
}
