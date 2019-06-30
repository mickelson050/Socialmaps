package com.example.socialmaps;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.socialmaps.model.SaveSharedPreference;
import com.example.socialmaps.model.TestSender;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import static com.example.socialmaps.util.Constants.ERROR_DIALOG_REQUEST;
import static com.example.socialmaps.util.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.socialmaps.util.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;

public class MainActivity extends AppCompatActivity {

    private boolean mLocationPermissionGranted = false;
    private FusedLocationProviderClient mFusedLocationClient;

    private static final String TAG = "MainActivity";

    public static double lat;
    public static double lon;

    private TestSender t;

    private EditText loginEmail;
    private EditText loginPassword;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAlreadyLogin();

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginEmail= (EditText) findViewById(R.id.loginEmail);
                loginPassword= (EditText) findViewById(R.id.loginPassword);
                login();
            }
        });

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }

    private void checkAlreadyLogin() {
        String savedToken = SaveSharedPreference.getToken(MainActivity.this);
        HashMap<String, String> loggedMap = new HashMap<String, String>();

        loggedMap.put("token", savedToken);

        t = new TestSender();
        t.doThePost("http://socialmaps.openode.io/api/mobileVerifyToken",loggedMap);

        waitForLoggedResp();
    }

    private synchronized void waitForLoggedResp() {
        while (t.getResp()==null);
        Log.v(TAG,t.getResp());
        String resp = t.getResp();
        t.resetResp();
        if(resp.contains("goodToken")) {
            Log.v(TAG,"Good token saved, loggin in...");

            Intent i = new Intent(MainActivity.this, DashboardActivity.class);
            finish();  //Kill the activity from which you will go to next activity
            startActivity(i);
        } else {
            Log.v(TAG,"Bas token save, doing nothing...");
        }
    }

    private static String getmd5(String password){
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(password.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void login() {

        String hashedPass = getmd5(loginPassword.getText().toString());
        Log.v(TAG, hashedPass);

        HashMap<String, String> loginMap = new HashMap<String, String>();

        loginMap.put("email", loginEmail.getText().toString());
        loginMap.put("password", hashedPass);

        t = new TestSender();
        t.doThePost("http://socialmaps.openode.io/api/login",loginMap);

        waitForResp();
    }

    private synchronized void waitForResp() {
        while (t.getResp()==null);
        Log.v(TAG,t.getResp());
        String resp = t.getResp();
        t.resetResp();
        if(resp.contains("token")) {
            Log.v(TAG,"Valid login");
            try {
                JSONObject loginResp = new JSONObject(resp);
                putInPrefs(loginResp);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent i = new Intent(MainActivity.this, DashboardActivity.class);
            finish();  //Kill the activity from which you will go to next activity
            startActivity(i);
        } else {
            Log.v(TAG,"Invalid login");
        }
    }

    private void putInPrefs(JSONObject loginResp) throws JSONException {
        String token = loginResp.getString("token");
        JSONObject user = loginResp.getJSONObject("userObject");
        String userName = user.getString("username");
        String userID = user.getString("_id");
        Log.v(TAG,"Token: " + token + " Username: " + userName + " UserID: " + userID);
        SaveSharedPreference.setUserName(MainActivity.this, userName);
        SaveSharedPreference.setUserID(MainActivity.this, userID);
        SaveSharedPreference.setToken(MainActivity.this, token);
        Log.v(TAG,"Preferences!! Token: " + SaveSharedPreference.getToken(MainActivity.this) + " Username: " + SaveSharedPreference.getUserName(MainActivity.this) + " UserID: " + SaveSharedPreference.getUserID(MainActivity.this));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getLastKnownLocation() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful()) {
                    Location location = task.getResult();
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                    Log.d(TAG, "onComplete: lat: "+lat+" lon: "+lon);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        if(checkMapServices()) {
            if(mLocationPermissionGranted) {
                doStuff();
                getLastKnownLocation();
            } else {
                getLocationPermission();
            }
        }
    }

    private void doStuff() {
        Toast.makeText(this, "Everything is fine", Toast.LENGTH_LONG).show();
    }

    private boolean checkMapServices(){
        if(isServicesOK()){
            if(isMapsEnabled()){
                return true;
            }
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            doStuff();
            getLastKnownLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOK(){

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if(mLocationPermissionGranted){
                    doStuff();
                    getLastKnownLocation();
                }
                else{
                    getLocationPermission();
                }
            }
        }

    }
}
