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
import androidx.fragment.app.FragmentResultListener;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ineedyourcode.donotes.R;
import com.ineedyourcode.donotes.domain.Note;
import com.ineedyourcode.donotes.domain.NotesRepositoryBuffer;
import com.ineedyourcode.donotes.ui.MainActivity;
import com.ineedyourcode.donotes.ui.dialogalert.AlertDialogFragment;

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

        getParentFragmentManager()
                .setFragmentResultListener(AlertDialogFragment.KEY_RESULT, this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        AlertDialogFragment adf = (AlertDialogFragment)getParentFragmentManager().findFragmentByTag("AlertDialogFragment");
                        switch (result.getInt(AlertDialogFragment.ARG_BUTTON)) {
                            case R.id.btn_dialog_ok:
                                requireActivity().finish();
                                Toast.makeText(requireContext(), "EXIT APP", Toast.LENGTH_SHORT).show();
                                adf.dismiss();
                                break;
                            case R.id.btn_dialog_cancel:
                                Toast.makeText(requireContext(), "\"Cancel\" pressed", Toast.LENGTH_SHORT).show();
                                adf = (AlertDialogFragment)getParentFragmentManager().findFragmentByTag("AlertDialogFragment");
                                adf.dismiss();
                                break;
                        }
                    }
                });

        notesContainer = view.findViewById(R.id.notes_container);

        FloatingActionButton fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "Add new note", Toast.LENGTH_SHORT).show();
            }
        });

        BottomAppBar bar = view.findViewById(R.id.bar);
        bar.replaceMenu(R.menu.menu_bottom_bar_note_list);
        ((MainActivity) requireActivity()).setToolbar(bar);

        bar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_item_sync_notes_list:
                    Toast.makeText(requireContext(), "Sync notes", Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.menu_item_search:
                    Toast.makeText(requireContext(), "Search notes", Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.menu_item_exit_app:
                    showAlertFragmentDialog("Exit app?");
                    return true;
            }
            return false;
        });

        presenter.updateNotesList(requireContext());
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

    private void showAlertFragmentDialog(String message) {
        AlertDialogFragment.newInstance(message)
                .show(getParentFragmentManager(), "AlertDialogFragment");
    }
}