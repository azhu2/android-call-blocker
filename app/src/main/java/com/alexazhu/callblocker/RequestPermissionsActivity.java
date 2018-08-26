package com.alexazhu.callblocker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class RequestPermissionsActivity extends AppCompatActivity {
    private static final String LOG_TAG = RequestPermissionsActivity.class.getSimpleName();

    private static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE};
    private static final int PERMISSIONS_REQUEST_CODE = 1;

    private Button permissionsButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_permissions);

        permissionsButton = findViewById(R.id.permissions_button);
        permissionsButton.setOnClickListener(new PermissionsButtonListener());
    }

    @Override
    protected void onStart() {
        super.onStart();

        final boolean permissionsGranted = checkPermissions();
        if (!permissionsGranted) {
            permissionsButton.setEnabled(true);
        }
    }

    private boolean checkPermissions() {
        Log.d(LOG_TAG, "Checking permissions");

        final Set<String> missingPermissions = new HashSet<>();
        for (final String permission : REQUIRED_PERMISSIONS) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }

        return missingPermissions.isEmpty();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final String permissions[], final int[] grantResults) {
        if (requestCode !=  PERMISSIONS_REQUEST_CODE) {
            return;
        }

        if (grantResults.length != permissions.length) {
            final String errorMsg = "Permissions not granted; exiting app";
            resetOnMissingPermissions(errorMsg);
            return;
        }

        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                final String errorMsg = String.format("Permission %s not granted; exiting app", permissions[i]);
                resetOnMissingPermissions(errorMsg);
                return;
            }
        }

        Log.d(LOG_TAG, "Permissions granted");
    }

    private void resetOnMissingPermissions(final String errorMsg) {
        Log.e(LOG_TAG, errorMsg);
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG);
        permissionsButton.setEnabled(true);
    }

    private class PermissionsButtonListener implements View.OnClickListener {
        @Override
        public void onClick(final View v) {
            permissionsButton.setEnabled(false);
            requestPermissions(REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
        }
    }
}
