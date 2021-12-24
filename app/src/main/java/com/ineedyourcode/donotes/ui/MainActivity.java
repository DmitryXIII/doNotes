package com.ineedyourcode.donotes.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import com.ineedyourcode.donotes.R;
import com.ineedyourcode.donotes.domain.Note;
import com.ineedyourcode.donotes.ui.navdrawer.AboutAppFragment;
import com.ineedyourcode.donotes.ui.navdrawer.SettingsFragment;
import com.ineedyourcode.donotes.ui.list.NotesListFragment;
import com.ineedyourcode.donotes.ui.notecontent.NoteContentFragment;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity implements com.ineedyourcode.donotes.ui.bottombar.ToolbarSetter {
    private static String ARG_NOTE = "ARG_NOTE";
    private Note selectedNote;
    private DrawerLayout navDrawer;
    public static final String APP_PREFERENCES = "SETTINGS";
    public static final String APP_PREFERENCES_REPO_MODE = "REPO_MODE";

    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        getSupportFragmentManager()
                .setFragmentResultListener(SettingsFragment.KEY_RESULT, this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        if (result.getInt(SettingsFragment.ARG_BUTTON) == R.id.rb_internal_notes) {
                            editor.putString(APP_PREFERENCES_REPO_MODE, getString(R.string.repo_type_internal));
                            editor.apply();
                        } else if (result.getInt(SettingsFragment.ARG_BUTTON) == R.id.rb_firebase) {
                            editor.putString(APP_PREFERENCES_REPO_MODE, getString(R.string.repo_type_firebase));
                            editor.apply();
                        } else {
                            editor.putString(APP_PREFERENCES_REPO_MODE, getString(R.string.repo_type_random));
                            editor.apply();
                        }

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new NotesListFragment())
                                .commit();
                    }
                });


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
                                .add(R.id.fragment_container, NoteContentFragment.updateInstance(selectedNote))
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
                        menuAction(new AboutAppFragment(), AboutAppFragment.TAG);
                        return true;
                    case R.id.action_settings:
                        menuAction(new SettingsFragment(), SettingsFragment.TAG);
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
                .add(R.id.fragment_container, fragment, tag)
                .addToBackStack("")
                .commit();
        navDrawer.closeDrawer(GravityCompat.START);
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
                .replace(R.id.fragment_content_container_land, NoteContentFragment.updateInstance(selectedNote))
                .addToBackStack("")
                .commit();
    }

    @Override
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
}