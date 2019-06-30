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
import com.example.socialmaps.model.SaveSharedPreference;
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

import org.w3c.dom.Text;

import java.util.HashMap;


public class TextPostActivity extends AppCompatActivity {

    private static final String TAG = "TextPostActivity";

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
        contentTxt= (EditText) findViewById(R.id.contentText);
        saveBtn= (Button) findViewById(R.id.saveBtn);
        findViewById(R.id.saveBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postTheStuff();
            }
        });

    }

    private void postTheStuff() {
        HashMap<String, String> hmap = new HashMap<String, String>();

        /*Adding elements to HashMap*/
        hmap.put("user", SaveSharedPreference.getUserID(TextPostActivity.this));
        hmap.put("username", SaveSharedPreference.getUserName(TextPostActivity.this));
        hmap.put("lat", Double.toString(MainActivity.getLat()));
        hmap.put("lon", Double.toString(MainActivity.getLon()));
        hmap.put("content", contentTxt.getText().toString());
        hmap.put("public", "0");

        TestSender t = new TestSender();
        t.doThePost("http://socialmaps.openode.io/api/new_text_post",hmap);

        contentTxt.setText("");
    }

    public void postDone() {

    }

}
