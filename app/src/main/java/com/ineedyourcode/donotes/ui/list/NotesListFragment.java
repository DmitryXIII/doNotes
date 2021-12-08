package com.ineedyourcode.donotes.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ineedyourcode.donotes.R;
import com.ineedyourcode.donotes.domain.Note;
import com.ineedyourcode.donotes.domain.NotesRepositoryBuffer;

import java.util.List;

public class NotesListFragment extends Fragment implements NotesListView {
    public static String ARG_NOTE = "ARG_NOTE";
    public static String RESULT_KEY = "NotesListFragment_RESULT";

    private LinearLayout notesContainer;
    private NotesListPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new NotesListPresenter(this, new NotesRepositoryBuffer());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fabNew = view.findViewById(R.id.fab_add_new_note);
        fabNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "Add new note", Toast.LENGTH_SHORT).show();
            }
        });

        notesContainer = view.findViewById(R.id.notes_container);

        presenter.updateNotesList();
    }

    @Override
    public void showNotes(List<Note> notes) {
        for (Note note : notes) {
            View itemView = LayoutInflater.from(requireContext()).inflate(R.layout.item_note, notesContainer, false);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle data = new Bundle();
                    data.putParcelable(ARG_NOTE, note);

                    getParentFragmentManager()
                            .setFragmentResult(RESULT_KEY, data);
                }
            });

            TextView noteTitle = itemView.findViewById(R.id.note_title);
            noteTitle.setText(note.getNoteTitle());

            TextView noteCreateDate = itemView.findViewById(R.id.note_create_date);
            noteCreateDate.setText(note.getNoteCreateDate());

            notesContainer.addView(itemView);
        }
    }
}
