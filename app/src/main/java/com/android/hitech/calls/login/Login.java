package com.android.hitech.calls.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hitech.calls.R;
import com.android.hitech.calls.homepage.Home_nav;
import com.android.hitech.calls.homepage.SessionManager;
import com.google.android.gms.vision.text.Line;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.R.attr.textColor;


public class Login extends AppCompatActivity {
    String nameValue, error;
    Button login;
    EditText UserName, Password;
    SessionManager session;
    String serverURL;
    LinearLayout li;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String Name = "nameKey";
    public static final String user = "userKey";
    NetworkInfo nInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);
        li=(LinearLayout)findViewById(R.id.li1);
        login = (Button) findViewById(R.id.btnLogin);
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        nInfo = cm.getActiveNetworkInfo();
        if (nInfo != null && nInfo.isConnected()) {
        } else {
            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.li1),
                    "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
            mySnackbar.setAction("TURN ON", new MyUndoListener());
            mySnackbar.show();

           /* new AlertDialog.Builder(Login.this)
                    .setCancelable(false)
                    .setTitle("No Active Internet Connection")
                    .setMessage("Switch on the Internet or Connect to active WIFI")
                    .setNegativeButton(R.string.action_settings, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();*/
        }

        session = new SessionManager();
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserName = (EditText) findViewById(R.id.edtUserName);
                Password = (EditText) findViewById(R.id.edtPassword);
                String u = UserName.getText().toString();
                String p = Password.getText().toString();
                serverURL = "http://172.16.0.3/api/HiTechApp/LogIn?username=" + u + "&password=" + p;
                Log.i("RESULTRESULT", serverURL);
                new LongOperation().execute(serverURL);
            }
        });

    }
    public class MyUndoListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            // Code to undo the user's last action
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nInfo != null && nInfo.isConnected()) {
        } else {
            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.li1),
                    "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
            mySnackbar.setAction("TURN ON", new MyUndoListener());
            View snackBarView = mySnackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#ffff00"));
            TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.parseColor("#ffff00"));
            mySnackbar.show();}
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (nInfo != null && nInfo.isConnected()) {
        } else {
            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.li1),
                    "No Internet Connection", Snackbar.LENGTH_INDEFINITE);
            mySnackbar.setAction("TURN ON", new MyUndoListener());
            mySnackbar.show();
        }
    }

    private class LongOperation extends AsyncTask<String, String, String> {
        StringBuilder sb;
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Login.this);
            pd.setMessage("Please wait");
        }

        protected String doInBackground(String... urls) {
            BufferedReader reader;
            sb = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("hitechApiKey", "hitechApiX@123#");
                conn.setRequestProperty("Authorization", "Basic YW5kcm9pZDpoaXRlY2hBcGlYQDEyMyM=");
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            } catch (Exception ex) {
                return "Failed";
            }
        }

        protected void onPostExecute(String s) {
            pd.cancel();
            Log.i("RESULTRESULT", s);
            if (s != "Failed") {
                try {

                    JSONObject jsonRootObject = new JSONObject(s);
                    String uid = jsonRootObject.getString("UserId");
                    nameValue = jsonRootObject.getString("UserName");
                    session.setPreferences(getApplicationContext(), "status", "1");
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Name, nameValue);
                    editor.putString(user, uid);
                    editor.apply();
                    Intent i = new Intent(Login.this, Home_nav.class);
                    i.putExtra("username", nameValue);
                    i.putExtra("userid", uid);
                    startActivity(i);
                } catch (JSONException e) {
                    Log.i("RESULTRESULT_P", String.valueOf(e));
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(Login.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
            }
        }
    }

}





