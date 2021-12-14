package com.ineedyourcode.donotes.ui;
/*TODO:
    - исправить привязку к активити в боттомбарах
    - кастомный диалог (в стиле приложения) при выходе из списка заметок
    - тосты заменить на снэкбары
    - кастомный диалог (в стиле приложения) удалении заметки
  */


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import com.ineedyourcode.donotes.R;
import com.ineedyourcode.donotes.domain.Note;
import com.ineedyourcode.donotes.ui.dialogalert.AlertDialogFragment;
import com.ineedyourcode.donotes.ui.navdrawer.AboutAppFragment;
import com.ineedyourcode.donotes.ui.navdrawer.SettingsFragment;
import com.ineedyourcode.donotes.ui.list.NotesListFragment;
import com.ineedyourcode.donotes.ui.notecontent.NoteContentFragment;

public class MainActivity extends AppCompatActivity {
    private static String ARG_NOTE = "ARG_NOTE";
    private Note selectedNote;
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
                .setFragmentResultListener(NotesListFragment.RESULT_KEY, this, (requestKey, result) -> {
                    selectedNote = result.getParcelable(NotesListFragment.ARG_NOTE);

                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        showNoteContent();
                    } else {
                        getSupportFragmentManager()
                                .beginTransaction()
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .replace(R.id.fragment_container, NoteContentFragment.newInstance(selectedNote))
                                .addToBackStack("")
                                .commit();
                    }
                });

        navDrawer = findViewById(R.id.nav_drawer);

        NavigationView navigationView = findViewById(R.id.menu_nav_drawer);

        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            navigationView.setNavigationItemSelectedListener(item -> {
                switch (item.getItemId()) {
                    case R.id.action_about:
                        menuAction(new AboutAppFragment(), "aboutAppFragment");
                        return true;
                    case R.id.action_settings:
                        menuAction(new SettingsFragment(), "settingsFragment");
                        return true;
                }
                return false;
            });
        }

    }

    private void menuAction(Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_container, fragment, tag)
                .addToBackStack("")
                .commit();
        navDrawer.closeDrawer(GravityCompat.START);
    }

    public void setToolbar(Toolbar toolbar) {
        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this,
                    navDrawer,
                    toolbar,
                    R.string.appbar_scrolling_view_behavior,
                    R.string.appbar_scrolling_view_behavior
            );
            navDrawer.addDrawerListener(toggle);
            toggle.syncState();
        }
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
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.fragment_content_container_land, NoteContentFragment.newInstance(selectedNote))
                .addToBackStack("")
                .commit();
    }

    public void setSelectedNoteToNull() {
        selectedNote = null;
    }
}