package com.ineedyourcode.donotes.ui.navdrawer;

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
import com.ineedyourcode.donotes.ui.MainActivity;
import com.ineedyourcode.donotes.ui.list.NotesListFragment;

public class SettingsFragment extends Fragment {

    public static final String KEY_RESULT = "SettingsFragment";
    public static final String ARG_BUTTON = "ARG_BUTTON";

    public static final String TAG = "SettingsFragment";

    private TextView randomNotesDescription;
    private TextView internalNotesDescription;
    private TextView firebaseNotesDescription;
    private ViewGroup mContainer;
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

        mContainer = view.findViewById(R.id.radio_group);

        btnSave = view.findViewById(R.id.btn_settings_save);

        randomNotesDescription = view.findViewById(R.id.radio_random_notes_description);
        internalNotesDescription = view.findViewById(R.id.radio_internal_notes_description);
        firebaseNotesDescription = view.findViewById(R.id.radio_firebase_notes_description);

        rg = view.findViewById(R.id.radio_group);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_random_notes:
                        selectedItemId = R.id.rb_random_notes;

                        TransitionManager.beginDelayedTransition(mContainer);

                        randomNotesDescription.setVisibility(View.VISIBLE);
                        internalNotesDescription.setVisibility(View.GONE);
                        firebaseNotesDescription.setVisibility(View.GONE);
                        break;
                    case R.id.rb_internal_notes:
                        selectedItemId = R.id.rb_internal_notes;

                        TransitionManager.beginDelayedTransition(mContainer);

                        randomNotesDescription.setVisibility(View.GONE);
                        internalNotesDescription.setVisibility(View.VISIBLE);
                        firebaseNotesDescription.setVisibility(View.GONE);
                        break;
                    case R.id.rb_firebase:
                        selectedItemId = R.id.rb_firebase;

                        TransitionManager.beginDelayedTransition(mContainer);

                        randomNotesDescription.setVisibility(View.GONE);
                        internalNotesDescription.setVisibility(View.GONE);
                        firebaseNotesDescription.setVisibility(View.VISIBLE);
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