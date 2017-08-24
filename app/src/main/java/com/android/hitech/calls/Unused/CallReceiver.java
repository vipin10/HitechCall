package com.android.hitech.calls.Unused;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.hitech.calls.database.DbAutoSave;
import com.android.hitech.calls.homepage.Home_nav;
import com.android.hitech.calls.splash.MyNetwork;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CallReceiver extends BroadcastReceiver {
    String number, tab_row, dir, callDate,dates, ids, callDur, phone, id_user;
    String url = "http://172.16.0.3/api/HiTechApp/CreateCallLog";
    public static final String mypreference = "mypref";
    JSONObject jobj, jobj1;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
        number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
        if (stateStr != null) {
            if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                sendToHost();
            }
        }

    }

    public void sendToHost() {
        getSendData();
        StringRequest objectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {

                        jobj = new JSONObject(response);
                        //jobj1 = jobj.getJSONObject("RecordId");
                        tab_row = jobj.getString("RecordId");
                        System.out.println("the id is"+tab_row);
                        DbAutoSave dbAutoSave = new DbAutoSave(context);
                        dbAutoSave.insertData(callDate, tab_row);
                        senddatasms();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i("message fetched",response);
                    Toast.makeText(context, "Log Saved Sucessfully"+response, Toast.LENGTH_LONG).show();

                    context.startActivity(new Intent(context, Home_nav.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    Toast.makeText(context, "Error : Please Save Manually", Toast.LENGTH_LONG).show();
                    Log.i("error",response);
                    context.startActivity(new Intent(context, Home_nav.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String a=error.toString();
                Log.i("errorss",a);
                Toast.makeText(context, "Error : Please Save Manually", Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(context, Home_nav.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        })


        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                super.getHeaders();
                Map<String, String> hashMap = new HashMap<>();
                String credentials = "android:hitechApiX@123#";
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                hashMap.put("hitechApiKey","hitechApiX@123#");
                hashMap.put("Authorization", "Basic YW5kcm9pZDpoaXRlY2hBcGlYQDEyMyM=");
                return hashMap;

            }
            @Override
            public Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> mMap= new HashMap<>();
                mMap.put("Content-Type", "application/x-www-form-urlencoded");
                mMap.put("UserId", id_user);
                mMap.put("DateStart", dates);
                mMap.put("CallDuration", callDur);
                mMap.put("Direction", dir);
                mMap.put("Mobile", phone);
                System.out.println("the data is"+mMap);
                return mMap;
            }
        };

        MyNetwork.getInstance(context).addToRequestQueue(objectRequest);
    }

    public void senddatasms() {
        String address = "http://172.16.0.3/api/HiTechApp/SendMessage";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, address, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                super.getHeaders();
                Map<String, String> hashMap = new HashMap<>();
                String credentials = "android:hitechApiX@123#";
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                hashMap.put("hitechApiKey","hitechApiX@123#");
                hashMap.put("Authorization", "Basic YW5kcm9pZDpoaXRlY2hBcGlYQDEyMyM=");
                return hashMap;

            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                super.getParams();
                Map<String, String> hMap = new HashMap<>();
                hMap.put("Content-Type", "application/x-www-form-urlencoded");
                hMap.put("UserId", id_user);
                hMap.put("CallDuration", callDur);
                hMap.put("Direction", dir);
                hMap.put("Mobile", phone);
                return hMap;
            }
        };
        MyNetwork.getInstance(context).addToRequestQueue(stringRequest);
    }

    public void getSendData() {
        SharedPreferences sharedpreferences = context.getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        id_user = sharedpreferences.getString("userKey", "");
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC LIMIT 1");
        if (cursor != null) {
            int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
            int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
            int date = cursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
            int id = cursor.getColumnIndex(CallLog.Calls._ID);
            cursor.moveToLast();
            phone = cursor.getString(number);
            String callType = cursor.getString(type);
            callDate = cursor.getString(date);
            ids = cursor.getString(id);
            java.util.Date callDayTime = new java.util.Date(Long.valueOf(callDate));
            callDate = callDayTime.toString();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            dates = formatter.format(Date.parse(callDate));
            callDur = cursor.getString(duration);
            dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            cursor.close();
        }
    }
}
