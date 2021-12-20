package com.ineedyourcode.donotes.ui.notecontent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.ineedyourcode.donotes.domain.NotesRepositoryBuffer;
import com.ineedyourcode.donotes.ui.MainActivity;
import com.ineedyourcode.donotes.ui.bottombar.ToolbarSetter;
import com.ineedyourcode.donotes.ui.dialogalert.AlertDialogFragment;
import com.ineedyourcode.donotes.ui.dialogalert.BottomDialogFragment;

public class NoteContentFragment extends Fragment implements AddNoteView {
    public static String ARG_NOTE = "ARG_NOTE";
    public static String RESULT_KEY = "NoteContentFragment";
    public static final String KEY = "NoteContentFragment";

    private FloatingActionButton fab;
    private ProgressBar savingProgressBar;
    private AddNotePresenter presenter;
    TextView noteTitle;
    TextView noteContent;
    BottomAppBar bar;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new AddNotePresenter(this, NotesRepositoryBuffer.INSTANCE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noteTitle = view.findViewById(R.id.txt_note_title);
        noteContent = view.findViewById(R.id.txt_note_content);
        Note note = null;
        try {
            note = requireArguments().getParcelable(ARG_NOTE);
            noteTitle.setText(note.getTitle());
            noteContent.setText(note.getMessage());
        } catch (IllegalStateException e) {
            noteTitle.setText("New note");
            noteContent.setText("");
        }

        bar = view.findViewById(R.id.bar);

        Activity activity = requireActivity();
        if (activity instanceof ToolbarSetter) {
            ((ToolbarSetter) activity).setToolbar(bar);
        }

        bar.replaceMenu(R.menu.menu_bottom_bar_note_content);
        bar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_item_share_note:
                    Toast.makeText(requireContext(), getString(R.string.share_note_massage), Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.menu_item_attach_image:
                    showBottomDialog(getString(R.string.bottomsheet_attach_message));
                    return true;

                case R.id.menu_item_delete_note:
                    showAlertFragmentDialog(getString(R.string.alert_dialog_delete_message));
                    return true;
            }
            return false;
        });


        savingProgressBar = view.findViewById(R.id.saving_progress);
        ImageView close = view.findViewById(R.id.close_icon);
        fab = view.findViewById(R.id.fab);


        fab.setOnClickListener(v -> {
            hideKeyboardFrom(requireContext(), noteTitle);
            hideKeyboardFrom(requireContext(), noteContent);
            presenter.saveNote(noteTitle.getText().toString(), noteContent.getText().toString());
        });

        close.setOnClickListener(v -> {
            hideKeyboardFrom(requireContext(), noteTitle);
            hideKeyboardFrom(requireContext(), noteContent);

            ((MainActivity) requireActivity()).setSelectedNoteToNull();
            requireActivity().onBackPressed();
        });
    }

    private void showAlertFragmentDialog(String message) {
        setDialogListener(AlertDialogFragment.KEY_RESULT, AlertDialogFragment.ARG_BUTTON, getString(R.string.note_deleted_message));

        if (getParentFragmentManager().findFragmentByTag(AlertDialogFragment.getTAG()) == null) {
            AlertDialogFragment.newInstance(message)
                    .show(getParentFragmentManager(), AlertDialogFragment.getTAG());
        }
    }

    private void showBottomDialog(String message) {
        setDialogListener(BottomDialogFragment.KEY_RESULT, BottomDialogFragment.ARG_BUTTON, getString(R.string.attach_message));

        if (getParentFragmentManager().findFragmentByTag(BottomDialogFragment.getTAG()) == null) {
            BottomDialogFragment.newInstance(message)
                    .show(getParentFragmentManager(), BottomDialogFragment.getTAG());
        }
    }

    private void setDialogListener(String keyResult, String argument, String message) {
        getParentFragmentManager()
                .setFragmentResultListener(keyResult, this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        if (result.getInt(argument) == R.id.btn_dialog_ok) {
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void noteSaved(Note note) {
        Bundle result = new Bundle();
        result.putParcelable(ARG_NOTE, note);

        getParentFragmentManager()
                .setFragmentResult(KEY, result);
    }

    @Override
    public void showProgress() {
        fab.setVisibility(View.GONE);
        savingProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        fab.setVisibility(View.VISIBLE);
        savingProgressBar.setVisibility(View.GONE);
    }
}
