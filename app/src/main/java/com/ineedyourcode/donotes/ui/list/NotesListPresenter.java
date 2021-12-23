package com.ineedyourcode.donotes.ui.list;

import com.ineedyourcode.donotes.domain.Callback;
import com.ineedyourcode.donotes.domain.Note;
import com.ineedyourcode.donotes.domain.NotesRepository;
import com.ineedyourcode.donotes.ui.adapter.AdapterItem;
import com.ineedyourcode.donotes.ui.adapter.NoteAdapterItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NotesListPresenter {

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

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
                ArrayList<AdapterItem> adapterItems = new ArrayList<>();

                for (Note note : result) {
                    adapterItems.add(new NoteAdapterItem(note, note.getTitle(), dateFormat.format(note.getCreatedAt()) + " " + timeFormat.format(note.getCreatedAt())));
                }

                view.showNotes(adapterItems);

                if (adapterItems.isEmpty()) {
                    view.showEmpty();
                } else {
                    view.hideEmpty();
                }

                view.hideProgress();
            }

            @Override
            public void onError(Throwable error) {
                view.hideProgress();
            }
        });

    }

    public void onNoteAdded(Note note) {
        NoteAdapterItem adapterItem = new NoteAdapterItem(note, note.getTitle(), dateFormat.format(note.getCreatedAt()) + " " + timeFormat.format(note.getCreatedAt()));

        view.onNoteAdded(adapterItem);

        view.hideEmpty();
    }

    public void removeItem(Note selectedNote) {
        view.showProgress();

        repository.delete(selectedNote, new Callback<Void>() {
            @Override
            public void onSuccess(Void result) {
                view.hideProgress();
                view.onNoteRemoved(selectedNote);
            }

            @Override
            public void onError(Throwable error) {
                view.hideProgress();
            }
        });
    }
}
