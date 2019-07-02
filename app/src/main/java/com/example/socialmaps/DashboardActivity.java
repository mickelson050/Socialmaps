package com.example.socialmaps;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.socialmaps.model.SaveSharedPreference;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";

    private FusedLocationProviderClient mFusedLocationClient;

    public LocationManager mLocationManager;
    private long minTime = 0;
    private float minDistance = 0;

    public static double lat;
    public static double lon;

    private boolean locationManagerInit = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastKnownLocation();

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime,
                minDistance, mLocationListener);


        findViewById(R.id.toMapActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, MapActivity.class));
            }
        });

        findViewById(R.id.toTextActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, TextPostActivity.class));
            }
        });

        findViewById(R.id.toCameraActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, CameraActivity.class));
            }
        });

        findViewById(R.id.toFriendsActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, FriendsActivity.class));
            }
        });

        findViewById(R.id.followActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, FollowActivity.class));
            }
        });

        TextView userNameText = (TextView) findViewById(R.id.usernameText);
        userNameText.setText(SaveSharedPreference.getUserName(DashboardActivity.this));
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


    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            if(locationManagerInit == false) {
                locationManagerInit = true;
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            }
            Log.v(TAG, "onLocationChanged: lat: "+location.getLatitude()+" lon: "+location.getLongitude());
            lat = location.getLatitude();
            lon = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    public static Double getLat() {
        return lat;
    }


    public static Double getLon() {
        return lon;
    }

}
