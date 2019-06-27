package com.example.socialmaps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.example.socialmaps.model.PhotoSender;
import com.example.socialmaps.model.Sender;
import com.example.socialmaps.model.TestSender;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;


public class TextPostActivity extends AppCompatActivity {

    private static final String TAG = "TextPostActivity";

    public LocationManager mLocationManager;
    private long minTime = 0;
    private float minDistance = 0;

    public double lat;
    public double lon;

    String urlAddress="http://socialmaps.dx.am/new_text_post.php";
    EditText userTxt,contentTxt,publiTxt;
    Button saveBtn;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//INITIALIZE UI FIELDS
        userTxt= (EditText) findViewById(R.id.userText);
        contentTxt= (EditText) findViewById(R.id.contentText);
        publiTxt= (EditText) findViewById(R.id.publicText);
        saveBtn= (Button) findViewById(R.id.saveBtn);
        findViewById(R.id.saveBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postTheStuff();
            }
        });

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime,
                minDistance, mLocationListener);

    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            Log.d(TAG, "onLocationChanged: lat: "+location.getLatitude()+" lon: "+location.getLongitude());
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

    private void postTheStuff() {
        HashMap<String, String> hmap = new HashMap<String, String>();

        /*Adding elements to HashMap*/
        hmap.put("user", userTxt.getText().toString());
        hmap.put("lat", Double.toString(lat));
        hmap.put("lon", Double.toString(lon));
        hmap.put("content", contentTxt.getText().toString());
        hmap.put("public", publiTxt.getText().toString());

        TestSender t = new TestSender();
        t.doThePost("http://socialmaps.openode.io/api/new_text_post",hmap);

        userTxt.setText("");
        contentTxt.setText("");
        publiTxt.setText("");
    }

    public void postDone() {

    }

}
