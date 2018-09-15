package com.alexazhu.callblocker.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

public class PermissionsUtil {
    private static final String LOG_TAG = PermissionsUtil.class.getSimpleName();

    public static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.ANSWER_PHONE_CALLS, Manifest.permission.READ_CALL_LOG, Manifest.permission.CALL_PHONE};

    private final Context context;

    public PermissionsUtil(final Context context) {
        this.context = context;
    }

    public boolean checkPermissions() {
        Log.d(LOG_TAG, "Checking permissions");

        final Set<String> missingPermissions = new HashSet<>();
        for (final String permission : PermissionsUtil.REQUIRED_PERMISSIONS) {
            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }

        return missingPermissions.isEmpty();
    }
}
