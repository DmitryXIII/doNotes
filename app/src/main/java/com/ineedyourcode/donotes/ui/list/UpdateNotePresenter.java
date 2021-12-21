package com.ineedyourcode.donotes.ui.list;

import android.os.Bundle;

import com.ineedyourcode.donotes.R;
import com.ineedyourcode.donotes.domain.Callback;
import com.ineedyourcode.donotes.domain.Note;
import com.ineedyourcode.donotes.domain.NotesRepository;
import com.ineedyourcode.donotes.ui.notecontent.AddNoteView;

public class UpdateNotePresenter implements NotePresenter{

    public static String ARG_NOTE = "ARG_NOTE";
    public static final String KEY = "NoteContentFragment_UPDATE";

    private AddNoteView view;
    private NotesRepository repository;
    private Note note;

    public UpdateNotePresenter(AddNoteView view, NotesRepository repository, Note note) {
        this.view = view;
        this.repository = repository;
        this.note = note;

        view.setFabIcon(R.drawable.ic_baseline_edit_24);

        view.setTitle(note.getTitle());
        view.setMessage(note.getContent());
    }

    @Override
    public void onActionPressed(String title, String message) {
        view.showProgress();

        repository.update(note.getId(), title, message, new Callback<Note>() {
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
