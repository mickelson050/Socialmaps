package com.example.socialmaps;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.example.socialmaps.model.SaveSharedPreference;
import com.example.socialmaps.model.TestSender;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private MapView mMapView;

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    private GoogleMap map;

    private static final String TAG = "MapActivity";

    private TestSender t;
    private JSONArray mapPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        map.setOnMarkerClickListener(this);
        getMapPoints();
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        centerMapOnMyLocation();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void getMapPoints() {
        HashMap<String, String> hmap = new HashMap<String, String>();

        /*Adding elements to HashMap*/
        hmap.put("username", SaveSharedPreference.getUserName(MapActivity.this));

        t = new TestSender();
        t.doThePost("http://socialmaps.openode.io/api/getFollowerPosts",hmap);


        waitForPoints();

    }

    private synchronized void waitForPoints() {
        while (t.getResp()==null);
        Log.v(TAG,t.getResp());
        try {
            mapPoints = new JSONArray(t.getResp());
            placePointsOnMap();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void placePointsOnMap() throws JSONException {
        for (int i = 0; i < mapPoints.length(); i++) {
            JSONObject obj = mapPoints.getJSONObject(i);
            String username = obj.getString("username");
            Double lat = Double.parseDouble(obj.getString("lat"));
            Double lon = Double.parseDouble(obj.getString("lon"));
            String content = obj.getString("content");
            String _id = obj.getString("_id");
            map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(username).snippet(content));
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        String markTitle = marker.getTitle();

        Log.v(TAG, "Marker: " + markTitle);

        double latOffset =  0.0012;
        double lonOffset =  0.0024;

        double lat = DashboardActivity.getLat();
        double lon = DashboardActivity.getLon();

        double markerLat = marker.getPosition().latitude;
        double markerLon = marker.getPosition().longitude;

        Log.v(TAG, "Marker position: " + markerLat + " , " + markerLon);
        Log.v(TAG, "Your position: " + lat + " , " + lon);

        boolean inRange = false;

        // If user latitude is within the range of the marker
        if(markerLat-latOffset <= lat && markerLat+latOffset >= lat) {
            // And if user longitude is within the range of the marker
            if(markerLon-lonOffset <= lon && markerLon+lonOffset >= lon) {
                inRange = true;
                Log.v(TAG, "Content: " + marker.getSnippet());
                Log.v(TAG, "IN RANGE");

                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                dlgAlert.setMessage(marker.getSnippet());
                dlgAlert.setTitle("Message from " + marker.getTitle());
                dlgAlert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //dismiss the dialog
                            }
                        });
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            }
        }

        if(inRange == false) {
            AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Posted by: " + marker.getTitle());
            dlgAlert.setTitle("Message to far away");
            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                        }
                    });
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }

//        PolylineOptions rectOptions = new PolylineOptions()
//                .add(new LatLng(lat+latOffset, lon+lonOffset))
//                .add(new LatLng(lat+latOffset, lon-lonOffset))
//                .add(new LatLng(lat-latOffset, lon-lonOffset))
//                .add(new LatLng(lat-latOffset, lon+lonOffset))
//                .add(new LatLng(lat+latOffset, lon+lonOffset));
//
//        Polyline polyline = map.addPolyline(rectOptions);

        return true;

    }

    private void centerMapOnMyLocation() {

        LatLng myLocation = new LatLng(DashboardActivity.getLat(),
                DashboardActivity.getLon());

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
    }

}
