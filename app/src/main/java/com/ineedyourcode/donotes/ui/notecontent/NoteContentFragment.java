package com.ineedyourcode.donotes.ui.notecontent;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ineedyourcode.donotes.R;
import com.ineedyourcode.donotes.domain.Note;
import com.ineedyourcode.donotes.ui.MainActivity;
import com.ineedyourcode.donotes.ui.list.NotesListFragment;

public class NoteContentFragment extends Fragment {

    private static final String ARG_NOTE = "ARG_NOTE";

    public static NoteContentFragment newInstance(Note note) {
        NoteContentFragment fragment = new NoteContentFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(ARG_NOTE, note);
        fragment.setArguments(arguments);
        return fragment;
    }

    public NoteContentFragment() {
        super(R.layout.fragment_notes_content);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Note note = requireArguments().getParcelable(ARG_NOTE);

        TextView noteTitle = view.findViewById(R.id.txt_note_title);
        TextView noteContent = view.findViewById(R.id.txt_note_content);
        ImageView close = view.findViewById(R.id.close_icon);
        ImageView edit = view.findViewById(R.id.edit_icon);

        noteTitle.setText(note.getNoteTitle());
        noteContent.setText(note.getNoteContent());

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
                MainActivity.setSelectedNote();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "Edit note", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
