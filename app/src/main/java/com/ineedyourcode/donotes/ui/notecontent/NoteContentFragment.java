package com.ineedyourcode.donotes.ui.notecontent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ineedyourcode.donotes.R;
import com.ineedyourcode.donotes.domain.Note;
import com.ineedyourcode.donotes.ui.MainActivity;

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

        BottomAppBar bar = view.findViewById(R.id.bar);
        ((MainActivity) requireActivity()).setToolbar(bar);
        bar.replaceMenu(R.menu.menu_bottom_bar_note_content);
        bar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_item_share_note:
                    Toast.makeText(requireContext(), "Share this note", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.menu_item_attach_image:
                    Toast.makeText(requireContext(), "Go to gallery to attach some image", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.menu_item_delete_note:
                    Toast.makeText(requireContext(), "Delete this note", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        });

        TextView noteTitle = view.findViewById(R.id.txt_note_title);
        TextView noteContent = view.findViewById(R.id.txt_note_content);
        ImageView close = view.findViewById(R.id.close_icon);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        noteTitle.setText(note.getNoteTitle());
        noteContent.setText(note.getNoteContent());

        fab.setOnClickListener(v -> Toast.makeText(requireContext(), "Edit note content", Toast.LENGTH_SHORT).show());

        close.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).setSelectedNoteToNull();
            requireActivity().onBackPressed();
        });
    }
}
