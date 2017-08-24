
package com.android.hitech.calls.NearByAccounts;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.hitech.calls.R;
import com.android.hitech.calls.meeting.Meeting_Main_Exp;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Locale;

public class NewCodeExpandable extends AppCompatActivity {
    ExpandableListView explvlist;
    JSONArray jsonArray1;
    String latitude = "28.5356727";
    String longitude = "77.2674794";
    String city = "New Delhi";
    String distance = "5";
    ArrayList<String> listHeader = new ArrayList<>();
    ArrayList<String> list2 = new ArrayList<>();
    ArrayList<String> list3 = new ArrayList<>();
    ArrayList<String> list4 = new ArrayList<>();
    List<JSONArray> secondAddlist = new ArrayList<>();
    String name, isAccount, cityName, phonOffice;
    double lat, lng;
    List<android.location.Address> addresse;
    Geocoder geocoder;
    ActionBar actionBar;
    String arr_permi[]= {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    LocationManager locationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_code_expnd);
        actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("My Accounts");
        }
        explvlist = (ExpandableListView) findViewById(R.id.expnList);
        String url = "https://resume.globalhunt.in/androidservices/index.php/LocServices/nearaccount";
        new NearByAccount().execute(url);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

    }

    public class NearByAccount extends AsyncTask<String, Void, Void> {
        String jsonUserData;
        ProgressDialog progressDialog;
        String error2 = null;
        String savemeetdata = "";

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(NewCodeExpandable.this);
            progressDialog.setMessage("Loading...... ");
            progressDialog.show();
            try {
                savemeetdata += "&" + URLEncoder.encode("lat", "UTF-8") + "=" + latitude;
                savemeetdata += "&" + URLEncoder.encode("lng", "UTF-8") + "=" + longitude;
                savemeetdata += "&" + URLEncoder.encode("city", "UTF-8") + "=" + city;
                savemeetdata += "&" + URLEncoder.encode("distance", "UTF-8") + "=" + distance;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            BufferedReader reader;
            try {
                URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(savemeetdata);
                writer.flush();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line + "");
                }
                jsonUserData = builder.toString();

            } catch (Exception e) {
                error2 = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();

            if (error2 != null) {

                Toast.makeText(NewCodeExpandable.this, "Error is" + error2, Toast.LENGTH_SHORT).show();
            }
            try {
                JSONArray jsonArray = new JSONArray(jsonUserData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    name = jsonObject.getString("name");
                    listHeader.add(name);
                    phonOffice = jsonObject.getString("phone_office");
                    list2.add(phonOffice);
                    System.out.println("Phone_office  :  " + phonOffice);
                    isAccount = jsonObject.getString("is_account");
                    list3.add(isAccount);
                    System.out.println("Is_Account :  " + isAccount);
                    cityName = jsonObject.getString("city_name");
                    list4.add(cityName);
                    System.out.println("City_Name :  " + cityName);
                    jsonArray1 = jsonObject.getJSONArray("full_address");
                    secondAddlist.add(jsonArray1);
                }


            } catch (Exception e) {

                e.printStackTrace();
            }

            explvlist.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                int previousGroup = -1;
                @Override
                public void onGroupExpand(int groupPosition) {
                    if (groupPosition != previousGroup) {
                        explvlist.collapseGroup(previousGroup);
                        previousGroup = groupPosition;
                    }
                }
            });
            ParentLevel parentLevel = new ParentLevel(getApplicationContext(), listHeader,list2,list3,list4);
            explvlist.setAdapter(parentLevel);
        }
    }


    public class ParentLevel extends BaseExpandableListAdapter {
        Context context;
        ArrayList<String> listHeader;
        ArrayList<String> list2;
        ArrayList<String> list3;
        ArrayList<String> list4;

        public ParentLevel(Context context, ArrayList<String> listHeader,
                           ArrayList<String> list2, ArrayList<String> list3, ArrayList<String> list4) {

            this.context = context;
            this.listHeader = listHeader;
            this.list2 = list2;
            this.list3 = list3;
            this.list4 = list4;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.group_header, null);
            TextView groupHeaderTxt = (TextView) view.findViewById(R.id.groupHeader);
            groupHeaderTxt.setText(listHeader.get(groupPosition));
            return view;
        }


        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            CustExpListview SecondLevelexplv = new CustExpListview(NewCodeExpandable.this);
            SecondLevelexplv.setAdapter(new SecondLevelAdapter(secondAddlist,groupPosition));
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.new_code_child, null);
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.l1);
            layout.addView(SecondLevelexplv);
            TextView textChild1 = (TextView) view.findViewById(R.id.txt1);
            TextView textChild2 = (TextView) view.findViewById(R.id.txt2);
            TextView textChild3 = (TextView) view.findViewById(R.id.txt3);
            textChild1.setText(list2.get(groupPosition));
            textChild2.setText(list3.get(groupPosition));
            textChild3.setText(list4.get(groupPosition));
            return view;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupPosition;
        }

        @Override
        public int getGroupCount() {
            return listHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }


        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    public class CustExpListview extends ExpandableListView {
        public CustExpListview(Context context) {
            super(context);
        }
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(960, MeasureSpec.AT_MOST);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(600, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public class SecondLevelAdapter extends BaseExpandableListAdapter {
        List<JSONArray> jsonArrays1;
        JSONArray array;
        List<String> list11 = new ArrayList<>();
        public SecondLevelAdapter(List<JSONArray> nArrays, int count) {
            this.jsonArrays1 = nArrays;
            array = nArrays.get(count);
            for (int i=0;i<array.length();i++){
                try {
                    JSONObject obj =array.getJSONObject(i);
                    list11.add(obj.getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition)
        {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(parent.getContext().LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.new_code_child1, parent, false);
            TextView textView11 = (TextView) view.findViewById(R.id.textViewChild);
            textView11.setText(list11.get(childPosition));
            System.out.println("The Data is :" + list11);
            return view;
        }

        @Override
        public int getChildrenCount(int groupPosition)
        {
            return array.length();
        }

        @Override
        public Object getGroup(int groupPosition)
        {
            return groupPosition;
        }

        @Override
        public int getGroupCount()
        {
            return 1;
        }

        @Override
        public long getGroupId(int groupPosition)
        {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            TextView tv = new TextView(NewCodeExpandable.this);
            tv.setText("More....");
            tv.setTextSize(16);
            tv.setPadding(20,0,0,10);
            tv.setTypeface(Typeface.DEFAULT_BOLD);
            tv.setTextColor(Color.parseColor("#2F3C50"));
            return tv;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
     super.onOptionsItemSelected(item);
        NavUtils.navigateUpFromSameTask(this);
        return true;
    }
}
