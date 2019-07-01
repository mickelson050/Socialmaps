package com.example.socialmaps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmaps.model.SaveSharedPreference;
import com.example.socialmaps.model.TestSender;

import java.util.HashMap;

public class FollowActivity extends AppCompatActivity {

    private static final String TAG = "FollowActivity";
    TestSender t;
    private EditText username;
    private Button searchUsername;
    private TextView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        username = (EditText) findViewById(R.id.searchuser);
        searchUsername = (Button)findViewById(R.id.Search);
        list = (TextView)findViewById(R.id.searchlistview);
        searchUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUsers();
            }
        });
    }

    public void getUsers(){
        HashMap<String, String> hmap = new HashMap<String, String>();

        /*Adding elements to HashMap*/
        //hmap.put("username", SaveSharedPreference.getUserName(FollowActivity.this));
        hmap.put("username", username.getText().toString().trim());
        t = new TestSender();
        t.doThePost("http://socialmaps.openode.io/api/findPerson",hmap);

        waitForResponse();
    }

    private synchronized void waitForResponse() {
        while (t.getResp()==null);
        String resp = t.getResp();

        if(resp.equals("nothingFound")) {
            list.setText("No one is found");
        }else{
            list.setText("found ");
            Toast.makeText(this, ""+ t.getResp(), Toast.LENGTH_SHORT).show();
        }

    }
}
