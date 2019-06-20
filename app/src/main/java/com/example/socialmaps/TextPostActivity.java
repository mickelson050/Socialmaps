package com.example.socialmaps;

import android.content.Intent;
import android.os.Bundle;

import com.example.socialmaps.model.Sender;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TextPostActivity extends AppCompatActivity {

    String urlAddress="http://socialmaps.dx.am/new_text_post.php";
    EditText userTxt,contentTxt,publiTxt;
    Button saveBtn;
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
                Sender s=new Sender(TextPostActivity.this,urlAddress,userTxt,contentTxt,publiTxt);
                s.execute();
            }
        });

//        saveBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////START ASYNC TASK
//                Sender s=new Sender(TextPostActivity.this,urlAddress,userTxt,contentTxt,publiTxt);
//                s.execute();
//            }
//        });
    }

}
