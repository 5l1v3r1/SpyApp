package com.app.spyapp.receiver;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.app.spyapp.R;
import com.app.spyapp.common.WriteLog;

/**
 * Created by Ankit on 4/21/2016.
 */
public class DeviceAdminReceiver extends android.app.admin.DeviceAdminReceiver {
    static final String TAG = "DeviceAdminReceiver";

    /** Called when this application is approved to be a device administrator. */
    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Toast.makeText(context, R.string.device_admin_enabled,
                Toast.LENGTH_LONG).show();
        WriteLog.E(TAG, "onEnabled");
    }

    /** Called when this application is no longer the device administrator. */
    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        Toast.makeText(context, R.string.device_admin_disabled,
                Toast.LENGTH_LONG).show();
        WriteLog.E(TAG, "onDisabled");
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        super.onPasswordChanged(context, intent);
        WriteLog.E(TAG, "onPasswordChanged");
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        super.onPasswordFailed(context, intent);
        WriteLog.E(TAG, "onPasswordFailed");
    }

    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        super.onPasswordSucceeded(context, intent);
        WriteLog.E(TAG, "onPasswordSucceeded");
    }
}
