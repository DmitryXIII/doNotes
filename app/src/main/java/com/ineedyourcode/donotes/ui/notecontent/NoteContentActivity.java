package com.ineedyourcode.donotes.ui.notecontent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.res.Configuration;
import android.os.Bundle;

import com.ineedyourcode.donotes.R;
import com.ineedyourcode.donotes.domain.Note;

public class NoteContentActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE = "EXTRA_NOTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_content);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
        } else {
            if (savedInstanceState == null) {
                FragmentManager fm = getSupportFragmentManager();
                Note note = getIntent().getParcelableExtra(EXTRA_NOTE);

                fm.beginTransaction()
                        .replace(R.id.note_content_container, NoteContentFragment.newInstance(note))
                        .commit();
            }
        }
    }
}