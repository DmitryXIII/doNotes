package com.ineedyourcode.donotes.ui.navdrawer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ineedyourcode.donotes.R;

public class SettingsFragment extends Fragment {

    public static final String KEY_RESULT = "SettingsFragment";
    public static final String ARG_BUTTON = "ARG_BUTTON";

    private static final String APP_PREFERENCES = "SETTINGS";
    private static final String APP_PREFERENCES_CHECKED_ID = "CHECKED_ID";

    public static final String TAG = "SettingsFragment";

    private TextView randomNotesDescription;
    private TextView internalNotesDescription;
    private TextView firestoreNotesDescription;
    private RadioGroup rg;
    private Button btnSave;
    private int selectedItemId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences mSettings = requireContext().getApplicationContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        btnSave = view.findViewById(R.id.btn_settings_save);

        randomNotesDescription = view.findViewById(R.id.radio_random_notes_description);
        internalNotesDescription = view.findViewById(R.id.radio_internal_notes_description);
        firestoreNotesDescription = view.findViewById(R.id.radio_firestore_notes_description);

        rg = view.findViewById(R.id.radio_group);

        if (mSettings.contains(APP_PREFERENCES_CHECKED_ID)) {
            selectedItemId = mSettings.getInt(APP_PREFERENCES_CHECKED_ID, selectedItemId);
        } else {
            selectedItemId = (R.id.rb_internal_notes);
        }

        rg.check(selectedItemId);

        switch (selectedItemId) {
            case R.id.rb_internal_notes:
                internalNotesDescription.setVisibility(View.VISIBLE);
                break;
            case R.id.rb_firestore:
                firestoreNotesDescription.setVisibility(View.VISIBLE);
                break;
            case R.id.rb_random_notes:
                randomNotesDescription.setVisibility(View.VISIBLE);
                break;
        }

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_random_notes:
                        selectedItemId = R.id.rb_random_notes;

                        TransitionManager.beginDelayedTransition(rg);

                        randomNotesDescription.setVisibility(View.VISIBLE);
                        internalNotesDescription.setVisibility(View.GONE);
                        firestoreNotesDescription.setVisibility(View.GONE);
                        break;
                    case R.id.rb_internal_notes:
                        selectedItemId = R.id.rb_internal_notes;

                        TransitionManager.beginDelayedTransition(rg);

                        randomNotesDescription.setVisibility(View.GONE);
                        internalNotesDescription.setVisibility(View.VISIBLE);
                        firestoreNotesDescription.setVisibility(View.GONE);
                        break;
                    case R.id.rb_firestore:
                        selectedItemId = R.id.rb_firestore;

                        TransitionManager.beginDelayedTransition(rg);

                        randomNotesDescription.setVisibility(View.GONE);
                        internalNotesDescription.setVisibility(View.GONE);
                        firestoreNotesDescription.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putInt(ARG_BUTTON, selectedItemId);
                getParentFragmentManager()
                        .setFragmentResult(KEY_RESULT, bundle);

                requireActivity().onBackPressed();
            }
        });
    }
}