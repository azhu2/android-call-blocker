package com.alexazhu.callblocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class IncomingCallReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = IncomingCallReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "Incoming call detected");
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
