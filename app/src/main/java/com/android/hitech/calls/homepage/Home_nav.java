package com.android.hitech.calls.homepage;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.hitech.calls.Alert_popup.About;
import com.android.hitech.calls.Location.MapsActivity;
import com.android.hitech.calls.Location.Servicebackground;
import com.android.hitech.calls.R;
import com.android.hitech.calls.call_logs.Call_logs;
import com.android.hitech.calls.dashboard.Dashboard;
import com.android.hitech.calls.login.Login;
import com.android.hitech.calls.splash.MyNetwork;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Home_nav extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    SessionManager session;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String Name = "nameKey";
    public static final String user = "userKey";
    FragmentTransaction ft;
    Fragment fragment;
    Button btnInstall, btnCancel;
    int itemid;
    MyDialog myDialog;
    View view;
    LayoutInflater inflater;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_nav);
        startService(new Intent(this, Servicebackground.class));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.Fragment_container, new Dashboard());
        tx.commit();
        session = new SessionManager();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView tv = (TextView) header.findViewById(R.id.u1);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Name)) {
            tv.setText(sharedpreferences.getString(Name, ""));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        itemid = item.getItemId();
        ft = getSupportFragmentManager().beginTransaction();
        CountDownTimer cdt = new CountDownTimer(500, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                switch (itemid) {
                    case R.id.nav_dashboard:
                        fragment = new Dashboard();
                        ft.replace(R.id.Fragment_container, fragment);
                        ft.commit();
                        break;
                    case R.id.nav_call:
                        fragment = new Call_logs();
                        ft.replace(R.id.Fragment_container, fragment);
                        ft.commit();
                        break;
                    case R.id.nav_location:
                        Intent i = new Intent(Home_nav.this, MapsActivity.class);
                        startActivity(i);
                        break;
                    case R.id.nav_schedule:
                        Toast.makeText(Home_nav.this, "Currently this option not Enabled for you.", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_about:
                        fragment = new About();
                        ft.replace(R.id.Fragment_container, fragment);
                        ft.commit();
                        break;
                    case R.id.nav_logout:
                        session.setPreferences(Home_nav.this, "status", "0");
                        Intent in = new Intent(Home_nav.this, Login.class);
                        startActivity(in);
                }
            }
        };
        cdt.start();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        item.setChecked(true);
        inflater = LayoutInflater.from(this);
        switch (item.getItemId()) {
            case R.id.update:
                updateOperation();
                return true;
            case R.id.versionn:
                myDialog = new MyDialog(this);
                View v = inflater.inflate(R.layout.version, null, false);
                myDialog.setCancelable(false);
                myDialog.setView(v);
                myDialog.show();
                myDialog.findViewById(R.id.verCancel).setOnClickListener(this);
                return true;
            case R.id.other_app:
                Toast.makeText(this, "Currently this option not Enabled for you.", Toast.LENGTH_LONG).show();
                return true;
        }
        return false;
    }

    private void updateOperation() {
        myDialog = new MyDialog(this);
        view = inflater.inflate(R.layout.update, null);
        myDialog.setView(view);
        myDialog.setCancelable(false);
        myDialog.show();
        btnInstall = (Button) myDialog.findViewById(R.id.btnInstall);
        btnCancel = (Button) myDialog.findViewById(R.id.cancel);
        if (btnInstall != null) {
            btnInstall.setOnClickListener(this);
            btnCancel.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnInstall) {
            LinearLayout ll = (LinearLayout) view.findViewById(R.id.root);
            pb = (ProgressBar) inflater.inflate(R.layout.customprogressbar, null, false);
            if (ll.getChildCount() < 3) {
                ll.addView(pb, 1);
            }

            if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                new UpdateTask().execute("https://resume.globalhunt.in/androidservices/location.apk");
                btnInstall.setVisibility(View.INVISIBLE);
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 78);
            }
        } else {
            myDialog.cancel();
        }
    }

    private class MyDialog extends AlertDialog {

        protected MyDialog(@NonNull Context context) {
            super(context);
        }
    }

    private class UpdateTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.connect();
                int lenghtOfFile = c.getContentLength();
                String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/";
                File file = new File(dir);
                file.mkdirs();
                File outputFile = new File(file, "location.apk");
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();
                byte[] buffer = new byte[1024];
                long total = 0;
                int len1;
                while ((len1 = is.read(buffer)) != -1) {
                    total += len1;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Download/" + "location.apk")), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } catch (FileNotFoundException fnfe) {
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            pb.setProgress(Integer.parseInt(values[0]));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String serverURL = "https://resume.golbalhunt.in/androidservices/index.php/api/Service/version";
            StringRequest request = new StringRequest(Request.Method.POST, serverURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    super.getParams();
                    Map<String, String> hashMap = new HashMap<>();
                    String idofuser = sharedpreferences.getString("userKey", "");
                    hashMap.put("uid", idofuser);
                    hashMap.put("version", "1.2.5");
                    return hashMap;
                }
            };

            MyNetwork.getInstance(Home_nav.this).addToRequestQueue(request);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        new UpdateTask().execute("https://resume.golbalhunt.in/androidservices/location.apk");
        btnInstall.setVisibility(View.INVISIBLE);
    }
}
