package com.ineedyourcode.donotes.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentResultListener;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.ineedyourcode.donotes.R;
import com.ineedyourcode.donotes.domain.Note;
import com.ineedyourcode.donotes.ui.list.NotesListFragment;
import com.ineedyourcode.donotes.ui.notecontent.NoteContentActivity;
import com.ineedyourcode.donotes.ui.notecontent.NoteContentFragment;

public class MainActivity extends AppCompatActivity {
    private static String ARG_NOTE = "ARG_NOTE";
    private Note selectedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_NOTE)) {
            selectedNote = savedInstanceState.getParcelable(ARG_NOTE);

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                showNoteContent();
            }
        }

        getSupportFragmentManager()
                .setFragmentResultListener(NotesListFragment.RESULT_KEY, this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        selectedNote = result.getParcelable(NotesListFragment.ARG_NOTE);

                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                            showNoteContent();
                        } else {
                            Intent intent = new Intent(MainActivity.this, NoteContentActivity.class);
                            intent.putExtra(NoteContentActivity.EXTRA_NOTE, selectedNote);
                            startActivity(intent);
                        }
                    }
                });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (selectedNote != null) {
            outState.putParcelable(ARG_NOTE, selectedNote);
        }
    }

    private void showNoteContent() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_content_container, NoteContentFragment.newInstance(selectedNote))
                .commit();
    }
}