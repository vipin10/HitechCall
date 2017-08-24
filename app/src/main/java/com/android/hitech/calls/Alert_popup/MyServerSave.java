package com.android.hitech.calls.Alert_popup;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyServerSave extends Service {
    //String serverURL = "https://resume.globalhunt.in/androidservices/index.php/api/Service/save";
    String serverURL="http://172.16.0.3/api/HiTechApp/CreateCallLog";
    String idOfUser, oppurdata = "", callTime, callDir, callPhone, callDuration,date, outResult;
    List<String> list = new ArrayList<>();
    int i;
    ArrayList<String> phNames, time, calldura, calldirection;
    boolean bool = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bool = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (bool) {
            idOfUser = intent.getStringExtra("idofuser");
            phNames = intent.getStringArrayListExtra("ph");
            time = intent.getStringArrayListExtra("time");
            calldura = intent.getStringArrayListExtra("calldura");
            calldirection = intent.getStringArrayListExtra("dir");
            for (i = 0; i < phNames.size(); i++) {
                callTime = time.get(i);
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                date = formatter.format(Date.parse(callTime));
                callDuration = calldura.get(i);
                callDir = calldirection.get(i);
                callPhone = phNames.get(i);
                saveAllCalls(date, callDuration, callDir, callPhone);
            }
        }
        bool = false;
        return START_NOT_STICKY;
    }

    private void saveAllCalls(String date, String callDuration, String callDir, String callPhone) {
        try {
            oppurdata += "&" + URLEncoder.encode("UserId", "UTF-8") + "=" + idOfUser;
            oppurdata += "&" + URLEncoder.encode("DateStart", "UTF-8") + "=" + date;
            oppurdata += "&" + URLEncoder.encode("CallDuration", "UTF-8") + "=" + callDuration;
            oppurdata += "&" + URLEncoder.encode("Direction", "UTF-8") + "=" + callDir;
            oppurdata += "&" + URLEncoder.encode("Mobile", "UTF-8") + "=" + callPhone;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.i("aaaaaaaa", oppurdata);
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader reader;
                try {
                    URL url = new URL(serverURL + oppurdata);
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                    wr.write(oppurdata);
                    wr.flush();
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    outResult = sb.toString();
                    list.add(outResult);
                    System.out.println("aaaaaaaa:" + list);
                } catch (Exception ex) {
                    System.out.println("aaaaaaaaE:" + ex);
                }
            }
        }).start();



        oppurdata = "";
    }
}
