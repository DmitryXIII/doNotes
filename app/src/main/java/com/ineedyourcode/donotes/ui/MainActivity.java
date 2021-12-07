package com.ineedyourcode.donotes.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.ineedyourcode.donotes.R;
import com.ineedyourcode.donotes.domain.Note;
import com.ineedyourcode.donotes.navdrawer.AboutAppFragment;
import com.ineedyourcode.donotes.navdrawer.SettingsFragment;
import com.ineedyourcode.donotes.ui.list.NotesListFragment;
import com.ineedyourcode.donotes.ui.notecontent.NoteContentFragment;

public class MainActivity extends AppCompatActivity {
    private static String ARG_NOTE = "ARG_NOTE";
    private static Note selectedNote;
    private DrawerLayout navDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new NotesListFragment())
                    .commit();
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_NOTE)) {
            selectedNote = savedInstanceState.getParcelable(ARG_NOTE);

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && selectedNote != null) {
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
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container, NoteContentFragment.newInstance(selectedNote))
                                    .addToBackStack("")
                                    .commit();
                        }
                    }
                });

        navDrawer = findViewById(R.id.nav_drawer);

        NavigationView navigationView = findViewById(R.id.menu_nav_drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_about:
                        menuAction(new AboutAppFragment(), "aboutAppFragment");
                    case R.id.action_settings:
                        menuAction(new SettingsFragment(), "settingsFragment");
                }
                return false;
            }
        });
    }

    private boolean menuAction(Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .addToBackStack(tag)
                .commit();
        navDrawer.closeDrawer(GravityCompat.START);
        return true;
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
                .replace(R.id.fragment_content_container_land, NoteContentFragment.newInstance(selectedNote))
                .addToBackStack("")
                .commit();
    }

    public static void setSelectedNote() {
        MainActivity.selectedNote = null;
    }
}