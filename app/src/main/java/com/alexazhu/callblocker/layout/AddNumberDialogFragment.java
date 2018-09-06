package com.alexazhu.callblocker.layout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.alexazhu.callblocker.R;
import com.alexazhu.callblocker.activity.ConfigurationActivity;
import com.alexazhu.callblocker.blockednumber.BlockedNumber;
import com.alexazhu.callblocker.blockednumber.BlockedNumberType;

public class AddNumberDialogFragment extends DialogFragment {
    public static final String LAYOUT_ID_KEY = "layout";
    public static final String DIALOG_TYPE = "dialogType";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ConfigurationActivity configActivity = (ConfigurationActivity) getActivity();
        Bundle args = getArguments();

        final int layoutId = args.getInt(LAYOUT_ID_KEY);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = configActivity.getLayoutInflater();

        View dialogView = inflater.inflate(layoutId, null);
        EditText numberView = dialogView.findViewById(R.id.phone_number);

        builder.setView(dialogView)
            .setPositiveButton("Add", (dialog, id) -> {
                String phoneNumber = numberView.getText().toString();
                BlockedNumberType type = (BlockedNumberType) args.getSerializable(DIALOG_TYPE);
                BlockedNumber blockedNumber = new BlockedNumber(type, phoneNumber);
                configActivity.addNumber(blockedNumber);
            })
            .setNegativeButton("Cancel", (dialog, id) -> {});

        AlertDialog dialog = builder.create();

        numberView.setOnKeyListener((view, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
                return true;
            }
            return false;
        });

        return dialog;
    }
}
