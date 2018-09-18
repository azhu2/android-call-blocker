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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alexazhu.callblocker.R;
import com.alexazhu.callblocker.activity.ConfigurationActivity;
import com.alexazhu.callblocker.blockednumber.BlockedNumber;
import com.alexazhu.callblocker.blockednumber.BlockedNumberType;

public class AddNumberDialogFragment extends DialogFragment implements OnItemSelectedListener {
    private final String LOG_TAG = AddNumberDialogFragment.class.getSimpleName();

    public static final String LAYOUT_ID_KEY = "layout";
    public static final String DIALOG_TYPE = "dialogType";
    public static final String TITLE = "title";

    private Spinner countryList;
    private TextView countryCodeView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ConfigurationActivity configActivity = (ConfigurationActivity) getActivity();
        Bundle args = getArguments();

        AlertDialog.Builder builder = new AlertDialog.Builder(configActivity);
        LayoutInflater inflater = configActivity.getLayoutInflater();

        View dialogView = inflater.inflate(args.getInt(LAYOUT_ID_KEY), null);
        countryCodeView = dialogView.findViewById(R.id.country_code);
        EditText numberView = dialogView.findViewById(R.id.phone_number);
        countryList = dialogView.findViewById(R.id.country_list);
        CountryListAdapter adapter = new CountryListAdapter(configActivity, this);
        countryList.setAdapter(adapter);
        countryList.setOnItemSelectedListener(this);

        builder.setView(dialogView)
            .setPositiveButton("Add", (dialog, id) -> {
                String countryCode = countryCodeView.getText().toString();
                String phoneNumber = numberView.getText().toString();
                BlockedNumberType type = (BlockedNumberType) args.getSerializable(DIALOG_TYPE);
                try {
                    BlockedNumber blockedNumber = new BlockedNumber(type, countryCode, phoneNumber);
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

    public void selectCountry(int position) {
        countryList.setSelection(position);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        CountryListAdapter.Country country = (CountryListAdapter.Country) parent.getItemAtPosition(position);
        countryCodeView.setText("+" + country.getCountryCode());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        countryCodeView.setText("");
    }
}
