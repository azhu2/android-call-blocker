package com.alexazhu.callblocker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

public class ConfigureActivity extends AppCompatActivity {
    private static final String LOG_TAG = ConfigureActivity.class.getSimpleName();

    private static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE};
    private static final int PERMISSIONS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure);
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkAndRequestPermissions();
    }

    private void checkAndRequestPermissions() {
        Log.d(LOG_TAG, "Checking permissions");

        final Set<String> missingPermissions = new HashSet<>();
        for (final String permission : REQUIRED_PERMISSIONS) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }

        if (!missingPermissions.isEmpty()) {
            Log.d(LOG_TAG, "Requesting missing permissions");
            requestPermissions(missingPermissions.toArray(new String[0]), PERMISSIONS_REQUEST_CODE);
        } else {
            Log.d(LOG_TAG, "Permissions already granted");
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final String permissions[], final int[] grantResults) {
        if (requestCode !=  PERMISSIONS_REQUEST_CODE) {
            return;
        }

        if (grantResults.length != permissions.length) {
            Log.e(LOG_TAG, "Permissions not granted; exiting app");
            finish();
            return;
        }

        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
            Log.e(LOG_TAG, String.format("Permission %s not granted; exiting app", permissions[i]));
            finish();
            return;
        }

        Log.d(LOG_TAG, "Permissions granted");
    }
}
