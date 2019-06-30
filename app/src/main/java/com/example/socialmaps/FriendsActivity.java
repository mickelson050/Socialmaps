package com.example.socialmaps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.socialmaps.model.SaveSharedPreference;
import com.example.socialmaps.model.TestSender;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

public class FriendsActivity extends AppCompatActivity {

    private static final String TAG = "FriendsActivity";

    private TestSender t;

    private TextView listHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        listHeader = (TextView) findViewById(R.id.listHeader);


        getFollowers();
    }

    private void getFollowers() {
        HashMap<String, String> hmap = new HashMap<String, String>();

        /*Adding elements to HashMap*/
        hmap.put("username", SaveSharedPreference.getUserName(FriendsActivity.this));

        t = new TestSender();
        t.doThePost("http://socialmaps.openode.io/api/getFollowers",hmap);


        waitForResponse();

    }

    private synchronized void waitForResponse() {
        while (t.getResp()==null);
        Log.v(TAG,t.getResp());

        if(t.getResp() == "noFollowersFound") {
            listHeader.setText("You dont follow anyone yet");
        }

    }
}
