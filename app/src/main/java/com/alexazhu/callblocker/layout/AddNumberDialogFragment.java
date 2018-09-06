package com.alexazhu.callblocker.layout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alexazhu.callblocker.R;
import com.alexazhu.callblocker.activity.ConfigurationActivity;
import com.alexazhu.callblocker.blockednumber.BlockedNumber;
import com.alexazhu.callblocker.blockednumber.BlockedNumberType;

public class AddNumberDialogFragment extends DialogFragment {
    private final String LOG_TAG = AddNumberDialogFragment.class.getSimpleName();

    public static final String LAYOUT_ID_KEY = "layout";
    public static final String DIALOG_TYPE = "dialogType";
    public static final String TITLE = "title";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ConfigurationActivity configActivity = (ConfigurationActivity) getActivity();
        Bundle args = getArguments();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = configActivity.getLayoutInflater();

        View dialogView = inflater.inflate(args.getInt(LAYOUT_ID_KEY), null);
        EditText numberView = dialogView.findViewById(R.id.phone_number);

        builder.setView(dialogView)
            .setPositiveButton("Add", (dialog, id) -> {
                String phoneNumber = numberView.getText().toString();
                BlockedNumberType type = (BlockedNumberType) args.getSerializable(DIALOG_TYPE);
                try {
                    BlockedNumber blockedNumber = new BlockedNumber(type, phoneNumber);
                    configActivity.addNumber(blockedNumber);
                    Log.i(LOG_TAG, String.format("Added valid number: %s", blockedNumber));
                } catch (IllegalArgumentException e) {
                    Log.i(LOG_TAG, String.format("Tried to add invalid number: %s", phoneNumber));
                    Toast.makeText(configActivity, "Invalid number", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Cancel", (dialog, id) -> {})
            .setTitle(args.getString(TITLE));

        AlertDialog dialog = builder.create();

        dialog.setOnKeyListener((view, keyCode, event) -> {
            if (event.getAction() != KeyEvent.ACTION_DOWN) {
                return false;
            }
            switch (keyCode) {
                case KeyEvent.KEYCODE_ENTER:
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
                    return true;
                default:
                    return false;
            }
        });

        return dialog;
    }
}
