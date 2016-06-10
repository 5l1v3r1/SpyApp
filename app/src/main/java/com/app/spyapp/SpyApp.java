package com.app.spyapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.app.spyapp.database.DatabaseHelper;


public class SpyApp extends Application {
    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;
    private String outgoingnumber;

    public String getOutgoingnumber() {
        return outgoingnumber;
    }

    public void setOutgoingnumber(String outgoingnumber) {
        this.outgoingnumber = outgoingnumber;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.openDataBase();
    }


    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}
