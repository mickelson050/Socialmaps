package com.example.socialmaps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmaps.model.Register;

public class RegisterActivity extends AppCompatActivity {
    String urlAdress = "http://socialmaps.dx.am/register.php";
    private EditText userName, userFirstName, userLastName, userPassword, userConfirmPassword, userEmail, userBirthdate;
    private RadioButton userMale, userFemale;
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
                if(register()){
                    Register r = new Register(RegisterActivity.this,urlAdress, userFirstName,userLastName,userName,userPassword,userBirthdate, userEmail);
                    r.execute();
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
        userName = (EditText) findViewById(R.id.registerUsername);
        userFirstName = (EditText) findViewById(R.id.registerFirstName);
        userLastName = (EditText) findViewById(R.id.registerLastName);
        userBirthdate = (EditText) findViewById(R.id.registerBirthdate);
        userPassword = (EditText) findViewById(R.id.registerUserPassword);
        userConfirmPassword = (EditText) findViewById(R.id.registerConfirmPassword);
        userEmail = (EditText) findViewById(R.id.registerEmail);
        userMale = (RadioButton) findViewById(R.id.registerMale);
        userFemale = (RadioButton) findViewById(R.id.registerFemale);
        signInActivity = (TextView) findViewById(R.id.loginActivity);

        register = (Button) findViewById(R.id.register); }

    private Boolean register() {
        boolean result = false;
        String username = userName.getText().toString();
        String password = userPassword.getText().toString();
        String confirmpassword = userConfirmPassword.getText().toString();
        String email = userEmail.getText().toString();
        String firstname = userFirstName.getText().toString();
        String lastName = userLastName.getText().toString();
        String birthdate = userBirthdate.getText().toString();
        String male = userMale.getText().toString();
        String female = userFemale.getText().toString();

        if (password.isEmpty() || confirmpassword.isEmpty()) {
            Toast.makeText(this, "Please fill the password in twice", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(confirmpassword)) {
            Toast.makeText(this, "please fill the same password in twice", Toast.LENGTH_SHORT).show();
        }

        else if (username.isEmpty()) {
            Toast.makeText(this, "Please fill in your username", Toast.LENGTH_SHORT).show();
        }

        else if (firstname.isEmpty()) {
            Toast.makeText(this, "Please fill in your first name", Toast.LENGTH_SHORT).show();
        }

        else if (lastName.isEmpty()) {
            Toast.makeText(this, "Please fill in your lastname", Toast.LENGTH_SHORT).show();
        }

        else if (email.isEmpty()) {
            Toast.makeText(this, "Please fill in your email", Toast.LENGTH_SHORT).show();

        }
        else if(birthdate.isEmpty()) {
            Toast.makeText(this, "Please fill in your birthdate", Toast.LENGTH_SHORT).show();
        }
        else if (male.isEmpty() && female.isEmpty()){
            Toast.makeText(this, "please fill in your gender", Toast.LENGTH_SHORT).show();
        }
        else {
            result = true;
        }

        return result;
    }

}
