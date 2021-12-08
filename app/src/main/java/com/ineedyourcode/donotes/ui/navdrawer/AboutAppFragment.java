package com.ineedyourcode.donotes.ui.navdrawer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ineedyourcode.donotes.R;

public class AboutAppFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_app, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnGitHub = view.findViewById(R.id.btn_github);
        btnGitHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gitUri = Uri.parse("https://github.com/DmitryXIII/doNotes");

                startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, gitUri), ""));
            }
        });
    }
}