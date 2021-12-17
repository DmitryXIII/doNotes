package com.ineedyourcode.donotes.ui.dialogalert;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ineedyourcode.donotes.R;

public class BottomDialogFragment extends BottomSheetDialogFragment {
    public static final String KEY_RESULT = "AlertDialogFragment";
    public static final String ARG_BUTTON = "ARG_BUTTON";

    private static final String TAG = "BottomDialogFragment";
    private static final String ARG_MESSAGE = "ARG_MESSAGE";

    public static BottomDialogFragment newInstance(String message) {
        BottomDialogFragment bottomDialogFragment = new BottomDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_MESSAGE, message);
        bottomDialogFragment.setArguments(bundle);
        return bottomDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
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
