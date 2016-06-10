package com.app.spyapp.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.app.spyapp.model.GeneralModel;

/**
 * Created by Ankit on 5/10/2016.
 */
public class GetgeneralInfoService extends IntentService {
    public GetgeneralInfoService() {
        super(GetgeneralInfoService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        getGeneralInfo();
    }

    public void getGeneralInfo() {

        TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = mngr.getDeviceId();
        String model = Build.MODEL;
        GeneralModel generalModel = new GeneralModel();
        generalModel.setIMEI(IMEI);
        generalModel.setModelNumber(model);
        generalModel.setOSVersion(String.valueOf(Build.VERSION.SDK_INT));
        generalModel.setConnectionType(checkNetworkStatus(this));
    }

    public String checkNetworkStatus(final Context context) {

        String networkStatus = "";

        // Get connect mangaer
        final ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // check for wifi
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        // check for mobile data
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable()) {
            networkStatus = "wifi";
        } else if (mobile.isAvailable()) {
            networkStatus = "mobileData";
        } else {
            networkStatus = "noNetwork";
        }

        return networkStatus;

    }

}
