package com.alexazhu.callblocker.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.alexazhu.callblocker.R;
import com.alexazhu.callblocker.util.PermissionsUtil;

public class RequestPermissionsActivity extends AppCompatActivity {
    private static final String LOG_TAG = RequestPermissionsActivity.class.getSimpleName();

    private static final int PERMISSIONS_REQUEST_CODE = 1;

    private PermissionsUtil permissionsUtil;
    private Button permissionsButton;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_permissions);

        permissionsButton = findViewById(R.id.permissions_button);
        permissionsButton.setOnClickListener(new PermissionsButtonListener());

        this.permissionsUtil = new PermissionsUtil(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final boolean permissionsGranted = permissionsUtil.checkPermissions();
        if (!permissionsGranted) {
            permissionsButton.setEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String permissions[], @NonNull final int[] grantResults) {
        if (requestCode !=  PERMISSIONS_REQUEST_CODE) {
            return;
        }

        if (grantResults.length != permissions.length) {
            final String errorMsg = "Permissions not granted";
            resetOnMissingPermissions(errorMsg);
            return;
        }

        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                final String errorMsg = String.format("Permission %s not granted", permissions[i]);
                resetOnMissingPermissions(errorMsg);
                return;
            }
        }

        Log.d(LOG_TAG, "Permissions granted");
        finish();       // Return to ConfigurationActivity
    }

    private void resetOnMissingPermissions(final String errorMsg) {
        Log.e(LOG_TAG, errorMsg);
        permissionsButton.setEnabled(true);
    }

    private class PermissionsButtonListener implements View.OnClickListener {
        @Override
        public void onClick(final View v) {
            permissionsButton.setEnabled(false);
            requestPermissions(PermissionsUtil.REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
        }
    }
}
