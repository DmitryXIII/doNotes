package com.ineedyourcode.donotes.ui.dialogalert;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ineedyourcode.donotes.R;

public class AlertDialogFragment extends DialogFragment {

    public static final String KEY_RESULT = "AlertDialogFragment";
    public static final String ARG_BUTTON = "ARG_BUTTON";

    private static final String TAG = "AlertDialogFragment";
    private static final String ARG_MESSAGE = "ARG_MESSAGE";

    public static AlertDialogFragment newInstance(String message) {
        AlertDialogFragment dialogAlertFragment = new AlertDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_MESSAGE, message);
        dialogAlertFragment.setArguments(bundle);
        return dialogAlertFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alert_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn_dialog_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(ARG_BUTTON, v.getId());

                getParentFragmentManager()
                        .setFragmentResult(KEY_RESULT, bundle);
                dismiss();
            }
        });

        view.findViewById(R.id.btn_dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), getString(R.string.button_cancel_message), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        TextView dialogMessage = view.findViewById(R.id.dialog_message);
        dialogMessage.setText(requireArguments().getString(ARG_MESSAGE));
    }

    public static String getTAG() {
        return TAG;
    }
}
