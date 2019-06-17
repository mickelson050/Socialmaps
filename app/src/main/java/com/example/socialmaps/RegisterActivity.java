package com.example.socialmaps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText userName, userPassword, userEmail;
    private Button register;
    private TextView signInActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        construct();

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(validate()){
                    //send data naar de database
                }
            }
        });

        signInActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
            }
        });
    }

    private void construct(){
        userName =(EditText)findViewById(R.id.registerusername);
        userPassword =(EditText)findViewById(R.id.registerUserPassword);
        userEmail =(EditText)findViewById(R.id.registerUserEmail);
        register = (Button)findViewById(R.id.registerUser);
        signInActivity = (TextView)findViewById(R.id.loginActivity);
    }

    private boolean validate(){
        Boolean result = false;

        String name = userName.getText().toString();
        String password = userPassword.getText().toString();
        String email = userEmail.getText().toString();

        if (name.isEmpty() && password.isEmpty() && email.isEmpty()){
            Toast.makeText(this,"Not everything is filled in", Toast.LENGTH_SHORT).show();
        }else {
            result =true;
        }
        return result;
    }
}
