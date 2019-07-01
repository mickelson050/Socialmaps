package com.example.socialmaps;

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
import android.view.View;
import android.widget.TextView;

import com.example.socialmaps.model.SaveSharedPreference;
import com.google.android.gms.location.LocationServices;

public class DashboardActivity extends AppCompatActivity {

    public LocationManager mLocationManager;
    private long minTime = 0;
    private float minDistance = 0;

    public static double lat;
    public static double lon;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

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

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            //Log.d(TAG, "onLocationChanged: lat: "+location.getLatitude()+" lon: "+location.getLongitude());
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
