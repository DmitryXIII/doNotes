package com.ineedyourcode.donotes.ui.notecontent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ineedyourcode.donotes.R;
import com.ineedyourcode.donotes.domain.Note;
import com.ineedyourcode.donotes.ui.MainActivity;
import com.ineedyourcode.donotes.ui.dialogalert.AlertDialogFragment;
import com.ineedyourcode.donotes.ui.dialogalert.BottomDialogFragment;

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

        getParentFragmentManager()
                .setFragmentResultListener(AlertDialogFragment.KEY_RESULT, this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        if (getParentFragmentManager().findFragmentByTag("AlertDialogFragment") != null) {
                            AlertDialogFragment adf = (AlertDialogFragment) getParentFragmentManager().findFragmentByTag("AlertDialogFragment");
                            switch (result.getInt(AlertDialogFragment.ARG_BUTTON)) {
                                case R.id.btn_dialog_ok:
                                    Toast.makeText(requireContext(), "Note deleted", Toast.LENGTH_SHORT).show();
                                    adf.dismiss();
                                    break;

                                case R.id.btn_dialog_cancel:
                                    Toast.makeText(requireContext(), "\"Cancel\" pressed", Toast.LENGTH_SHORT).show();
                                    adf.dismiss();
                                    break;
                            }
                        } else {
                            BottomDialogFragment bdf = (BottomDialogFragment) getParentFragmentManager().findFragmentByTag("BottomDialogFragment");
                            switch (result.getInt(AlertDialogFragment.ARG_BUTTON)) {
                                case R.id.btn_dialog_ok:
                                    Toast.makeText(requireContext(), "Attach file", Toast.LENGTH_SHORT).show();
                                    bdf.dismiss();
                                    break;

                                case R.id.btn_dialog_cancel:
                                    Toast.makeText(requireContext(), "\"Cancel\" pressed", Toast.LENGTH_SHORT).show();
                                    bdf.dismiss();
                                    break;
                            }
                        }
                    }
                });

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
                    showBottomDialog("Some options to attach file");
                    return true;

                case R.id.menu_item_delete_note:
                    showAlertFragmentDialog("Delete this note?");
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

    private void showAlertFragmentDialog(String message) {
        if (getParentFragmentManager().findFragmentByTag("AlertDialogFragment") == null) {
            AlertDialogFragment.newInstance(message)
                    .show(getParentFragmentManager(), "AlertDialogFragment");
        }
    }

    private void showBottomDialog(String message) {
        if (getParentFragmentManager().findFragmentByTag("BottomDialogFragment") == null) {
            BottomDialogFragment.newInstance(message)
                    .show(getParentFragmentManager(), "BottomDialogFragment");
        }
    }
}
