package com.ineedyourcode.donotes.ui.notecontent;

import android.os.Bundle;

import com.ineedyourcode.donotes.R;
import com.ineedyourcode.donotes.domain.Callback;
import com.ineedyourcode.donotes.domain.Note;
import com.ineedyourcode.donotes.domain.NotesRepository;
import com.ineedyourcode.donotes.ui.list.NotePresenter;

public class AddNotePresenter implements NotePresenter {
    public static String ARG_NOTE = "ARG_NOTE";
    public static final String KEY = "NoteContentFragment_ADD";


    private AddNoteView view;
    private NotesRepository repository;

    public AddNotePresenter(AddNoteView view, NotesRepository repository) {
        this.view = view;
        this.repository = repository;

        view.setFabIcon(R.drawable.ic_baseline_save_24);
    }

    @Override
    public void onActionPressed(String title, String content) {
        view.showProgress();
        repository.save(title, content, new Callback<Note>() {
            @Override
            public void onSuccess(Note result) {
                view.hideProgress();
                Bundle bundle = new Bundle();
                bundle.putParcelable(ARG_NOTE, result);
                view.actionCompleted(KEY, bundle);
            }

            @Override
            public void onError(Throwable error) {
                view.hideProgress();
            }
        });
    }
}
