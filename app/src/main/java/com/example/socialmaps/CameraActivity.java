package com.example.socialmaps;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.socialmaps.model.FileSender;
import com.example.socialmaps.model.TestSender;
import com.example.socialmaps.model.UploadFileAsync;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class CameraActivity extends AppCompatActivity {

    private static final String TAG = "CameraActivity";

    public LocationManager mLocationManager;
    private long minTime = 0;
    private float minDistance = 0;

    public double lat;
    public double lon;


    private ImageView cameraView;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;

    private Button takePictureButton;
    private ImageView imageView;

    private Uri file;

    String currentPhotoPath;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        takePictureButton = (Button) findViewById(R.id.cameraButton);
        imageView = (ImageView) findViewById(R.id.cameraImage);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

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


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePictureButton.setEnabled(true);
            }
        }
    }

    public void takePicture(View view) {
        if(lat != 0 && lon != 0) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            file = Uri.fromFile(getOutputMediaFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

            startActivityForResult(intent, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                imageView.setImageURI(file);
                Log.v(TAG, "saved file: " + file.toString());
                Log.v(TAG, "get external" + Environment.getExternalStorageDirectory());

                String fileDir = file.toString().substring(6);

                Log.v(TAG, "normal lat and lon: " + lat + lon);

                String latt= String.valueOf(lat);
                String lonn = String.valueOf(lon);

                Log.v(TAG, "parameter lat and lon: " + latt + lonn);

                UploadFileAsync ufa = new UploadFileAsync(fileDir,"88",latt,lonn,"0");
                ufa.execute("");
            }
        }
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "SocialMaps");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }


}
