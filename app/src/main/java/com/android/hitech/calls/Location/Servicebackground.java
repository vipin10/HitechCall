package com.android.hitech.calls.Location;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

public class Servicebackground extends Service {
    public double latitude;
    public double longitude;
    public static final String mypreference = "mypref";
    SharedPreferences sharedpreferences;
    public static final String user = "userKey";
    String idofuser, sedData;
    LocationManager lm;
    String provider;
    Location location;
    Timer timer = new Timer();
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.i("TheLatLngIs:", latitude + "," + longitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        idofuser = sharedpreferences.getString(user, "");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                lm = (LocationManager) getSystemService(LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                provider = lm.getBestProvider(criteria, true);
                location = lm.getLastKnownLocation(provider);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Log.i("TheLatLngIsLoc:", latitude + "," + longitude);
                } else {
                    Log.i("TheLatLngIs:", "iaMHere");
                    lm.requestSingleUpdate(provider, locationListener, Looper.getMainLooper());
                }
                System.out.println("TheLatLngIs:" + latitude + "," + longitude + "," + provider + "," + location);
                startTimerTask();
            }
        };
        timer.schedule(task, 5000, 10000);
        return START_STICKY;
    }

    private void startTimerTask() {
        String serverURL = "https://resume.golbalhunt.in/androidservices/index.php/LocServices/saveLoc";
        new Senddata().execute(serverURL);
        new SendGpsStatus().execute();
    }

    public class Senddata extends AsyncTask<String, Void, Void> {
        String oppurjson;
        String oppurerror = null;
        String oppurdata;

        protected void onPreExecute() {
            try {
                oppurdata += "&" + URLEncoder.encode("uid", "UTF-8") + "=" + idofuser;
                oppurdata += "&" + URLEncoder.encode("lat", "UTF-8") + "=" + latitude;
                oppurdata += "&" + URLEncoder.encode("lng", "UTF-8") + "=" + longitude;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        protected Void doInBackground(String... urls) {
            BufferedReader reader;
            try {

                URL url = new URL(urls[0]);
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
                oppurjson = sb.toString();
            } catch (Exception ex) {
                oppurerror = ex.getMessage();
            }
            return null;
        }

        protected void onPostExecute(Void unused) {

        }
    }

    private class SendGpsStatus extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (lm.isProviderEnabled(provider)) {
                try {
                    sedData += "&" + URLEncoder.encode("uid", "UTF-8") + "=" + idofuser;
                    sedData += "&" + URLEncoder.encode("uid", "UTF-8") + "=" + "1";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    sedData += "&" + URLEncoder.encode("uid", "UTF-8") + "=" + idofuser;
                    sedData += "&" + URLEncoder.encode("uid", "UTF-8") + "=" + "0";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("https://resume.golbalhunt.in/androidservices/index.php/LocServices/gps");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                OutputStreamWriter outputStream = new OutputStreamWriter(connection.getOutputStream());
                outputStream.write(sedData);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}



