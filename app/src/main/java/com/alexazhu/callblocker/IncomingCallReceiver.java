package com.alexazhu.callblocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class IncomingCallReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = IncomingCallReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        final String newState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        Log.d(LOG_TAG, String.format("Call state changed to %s", newState));

        if (TelephonyManager.EXTRA_STATE_RINGING.equals(newState)) {
            final String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if (phoneNumber == null) {
                Log.d(LOG_TAG, "Ignoring call; for some reason every state change is doubled");
            }
            Toast.makeText(context, "Incoming call from " + phoneNumber, Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG, String.format("Incoming call from %s", phoneNumber));
        }

        // TODO Handle call
    }
}
