package com.ineedyourcode.donotes.ui.notecontent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ineedyourcode.donotes.R;
import com.ineedyourcode.donotes.domain.internalrepo.InternalFileWriterRepository;
import com.ineedyourcode.donotes.domain.Note;
import com.ineedyourcode.donotes.ui.bottombar.ToolbarSetter;
import com.ineedyourcode.donotes.ui.dialogalert.AlertDialogFragment;
import com.ineedyourcode.donotes.ui.dialogalert.BottomDialogFragment;
import com.ineedyourcode.donotes.ui.list.NotePresenter;
import com.ineedyourcode.donotes.ui.list.UpdateNotePresenter;

public class NoteContentFragment extends Fragment implements AddNoteView {
    public static String ARG_NOTE = "ARG_NOTE";
    public static String KEY_RESULT = "NoteContentFragment";

    private FloatingActionButton fab;
    private ProgressBar savingProgressBar;
    private NotePresenter presenter;
    private EditText noteTitle;
    private EditText noteContent;
    private BottomAppBar bar;
    private Note note;

    public static NoteContentFragment addInstance() {
        return new NoteContentFragment();
    }

    public static NoteContentFragment updateInstance(Note note) {
        NoteContentFragment fragment = new NoteContentFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(ARG_NOTE, note);
        fragment.setArguments(arguments);
        return fragment;
    }

    public NoteContentFragment() {
        super(R.layout.fragment_notes_content);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        noteTitle = view.findViewById(R.id.txt_note_title);
        noteContent = view.findViewById(R.id.txt_note_content);
        fab = view.findViewById(R.id.fab);

        if (getArguments() == null) {
            presenter = new AddNotePresenter(this, InternalFileWriterRepository.getINSTANCE(requireContext()));
        } else {
            note = getArguments().getParcelable(ARG_NOTE);
            presenter = new UpdateNotePresenter(this, InternalFileWriterRepository.getINSTANCE(requireContext()), note);
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
                    shareNote(noteContent.getText().toString());
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

        fab.setOnClickListener(v -> {
            hideKeyboardFrom(requireContext(), noteTitle);
            hideKeyboardFrom(requireContext(), noteContent);
            presenter.onActionPressed(noteTitle.getText().toString(), noteContent.getText().toString());
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
        if (keyResult.equals("AlertDialogFragment")) {
            getParentFragmentManager()
                    .setFragmentResultListener(keyResult, this, new FragmentResultListener() {
                        @Override
                        public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                            if (result.getInt(argument) == R.id.btn_dialog_ok) {
                                requireActivity().onBackPressed();
                                Bundle bundle = new Bundle();
                                bundle.putParcelable(ARG_NOTE, note);
                                getParentFragmentManager()
                                        .setFragmentResult(KEY_RESULT, bundle);
                            }
                        }
                    });
        } else {
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
    }

    private void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void shareNote(String message) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType(getString(R.string.text_plain_type));
        intent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(intent, ""));
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

    @Override
    public void setFabIcon(int icon) {
        fab.setImageDrawable(ContextCompat.getDrawable(getContext(), icon));
    }

    @Override
    public void setTitle(String title) {
        noteTitle.setText(title);
    }

    @Override
    public void setMessage(String message) {
        noteContent.setText(message);
    }

    @Override
    public void actionCompleted(String key, Bundle bundle) {
        getParentFragmentManager()
                .setFragmentResult(key, bundle);
    }
}
