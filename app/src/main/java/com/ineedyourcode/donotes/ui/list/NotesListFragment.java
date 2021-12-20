package com.ineedyourcode.donotes.ui.list;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.ineedyourcode.donotes.R;
import com.ineedyourcode.donotes.domain.Note;
import com.ineedyourcode.donotes.domain.NotesRepositoryBuffer;
import com.ineedyourcode.donotes.ui.adapter.AdapterItem;
import com.ineedyourcode.donotes.ui.adapter.NoteAdapterItem;
import com.ineedyourcode.donotes.ui.bottombar.ToolbarSetter;
import com.ineedyourcode.donotes.ui.dialogalert.AlertDialogFragment;
import com.ineedyourcode.donotes.ui.notecontent.NoteContentFragment;

import java.util.List;

public class NotesListFragment extends Fragment implements NotesListView {
    public static String ARG_NOTE = "ARG_NOTE";
    public static String RESULT_KEY = "NotesListFragment_RESULT";
    public static String TAG = "NotesListFragment_RESULT";

    private RecyclerView notesContainer;
    private NotesListPresenter presenter;
    private NotesAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView emptyMessage;
    private Note selectedNote;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new NotesListPresenter(this, NotesRepositoryBuffer.INSTANCE);
        adapter = new NotesAdapter(this);
        adapter.setOnClick(new NotesAdapter.OnClick() {
            @Override
            public void onClick(Note note) {
                Bundle data = new Bundle();
                data.putParcelable(ARG_NOTE, note);

                getParentFragmentManager()
                        .setFragmentResult(RESULT_KEY, data);
            }

            @Override
            public void onLongClick(Note note) {
                selectedNote = note;
            }
        });
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
                        if (result.getInt(AlertDialogFragment.ARG_BUTTON) == R.id.btn_dialog_ok) {
                            requireActivity().finish();
                            Toast.makeText(requireContext(), getString(R.string.exit_message), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresher);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(requireContext().getColor(R.color.note_title));
        swipeRefreshLayout.setColorSchemeColors(requireContext().getColor(R.color.background_dark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.requestNotes();
            }
        });

        emptyMessage = view.findViewById(R.id.empty_list_message);

        notesContainer = view.findViewById(R.id.notes_container);
        notesContainer.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        notesContainer.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .add(R.id.fragment_container, new NoteContentFragment())
                        .addToBackStack("")
                        .commit();
            }
        });

        BottomAppBar bar = view.findViewById(R.id.bar);
        bar.replaceMenu(R.menu.menu_bottom_bar_note_list);

        Activity activity = requireActivity();
        if (activity instanceof ToolbarSetter) {
            ((ToolbarSetter) activity).setToolbar(bar);
        }

        bar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_item_sync_notes_list:
                    Snackbar snackbar = Snackbar.make(view, R.string.snack_sync_notes, Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(requireContext().getColor(R.color.note_title));
                    snackbar.setTextColor(requireContext().getColor(R.color.background_dark));
                    snackbar.show();
                    return true;

                case R.id.menu_item_search:
                    Toast.makeText(requireContext(), getString(R.string.search_notes_message), Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.menu_item_exit_app:
                    showAlertFragmentDialog(getString(R.string.alert_dialog_exit_message));
                    return true;
            }
            return false;
        });

        presenter.requestNotes();

        getParentFragmentManager()
                .setFragmentResultListener(NoteContentFragment.KEY, getViewLifecycleOwner(), new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        Note note = result.getParcelable(NoteContentFragment.ARG_NOTE);

                        presenter.onNoteAdded(note);
                    }
                });
    }

    @Override
    public void showNotes(List<AdapterItem> notes) {
        adapter.setData(notes);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showProgress() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showEmpty() {
        emptyMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmpty() {
        emptyMessage.setVisibility(View.GONE);
    }

    @Override
    public void onNoteAdded(NoteAdapterItem adapterItem) {
        int index = adapter.addItem(adapterItem);

        adapter.notifyItemInserted(index - 1);
        notesContainer.smoothScrollToPosition(index - 1);


    }

    @Override
    public void onNoteRemoved(Note selectedNote) {
        int index = adapter.removeItem(selectedNote);

        adapter.notifyItemRemoved(index);
    }

    private void showAlertFragmentDialog(String message) {
        AlertDialogFragment.newInstance(message)
                .show(getParentFragmentManager(), AlertDialogFragment.getTAG());
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = requireActivity().getMenuInflater();
        menuInflater.inflate(R.menu.notes_list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            presenter.removeItem(selectedNote);

            Toast.makeText(requireContext(), "Delete " + selectedNote.getTitle(), Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.action_update) {
            Toast.makeText(requireContext(), "Update " + selectedNote.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
    }
}