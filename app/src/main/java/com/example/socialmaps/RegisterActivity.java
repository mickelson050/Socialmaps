package com.example.socialmaps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmaps.model.Register;
import com.example.socialmaps.model.TestSender;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    String urlAdress = "http://socialmaps.dx.am/register.php";
    private EditText userName, userFirstName, userLastName, userPassword, userConfirmPassword, userEmail, userBirthdate, userGender, userHabitation;

    private Button register;
    private TextView signInActivity;
    private TestSender t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userName = (EditText) findViewById(R.id.registerUsername);
        userFirstName = (EditText) findViewById(R.id.registerFirstName);
        userLastName = (EditText) findViewById(R.id.registerLastName);
        userBirthdate = (EditText) findViewById(R.id.registerBirthdate);
        userPassword = (EditText) findViewById(R.id.registerUserPassword);
        userConfirmPassword = (EditText) findViewById(R.id.registerConfirmPassword);
        userEmail = (EditText) findViewById(R.id.registerEmail);
        userGender = (EditText) findViewById(R.id.registerGender);
        userHabitation = (EditText)findViewById(R.id.registerHabitation);

        signInActivity = (TextView) findViewById(R.id.loginActivity);

        register = (Button) findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(register()){
                    postTheStuff();
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

    public static String getmd5(String password){
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(password.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }




    private Boolean register() {
        boolean result = false;
        String username = userName.getText().toString();
        String password = userPassword.getText().toString();
        String confirmpassword = userConfirmPassword.getText().toString();
        String email = userEmail.getText().toString();
        String firstname = userFirstName.getText().toString();
        String lastName = userLastName.getText().toString();
        String birthdate = userBirthdate.getText().toString();
        String gender = userGender.getText().toString();
        String habitation = userHabitation.getText().toString();


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
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
        }
        else if(birthdate.isEmpty()) {
            Toast.makeText(this, "Please fill in your birthdate", Toast.LENGTH_SHORT).show();
        }
        else if (gender.isEmpty()){
            Toast.makeText(this, "Please fill in your gender", Toast.LENGTH_SHORT).show();
        }
        else if (habitation.isEmpty()){
            Toast.makeText(this, "Please fill in your habitation", Toast.LENGTH_SHORT).show();
        }
        else {
            result = true;
        }

        return result;
    }

    private void postTheStuff() {
        HashMap<String, String> hmap = new HashMap<String, String>();

        /*Adding elements to HashMap*/
        hmap.put("firstname", userFirstName.getText().toString().trim());
        hmap.put("lastname", userLastName.getText().toString().trim());
        hmap.put("username", userName.getText().toString().trim());
        hmap.put("password", getmd5(userPassword.getText().toString()));
        hmap.put("email", userEmail.getText().toString().trim());
        hmap.put("birthdate", userBirthdate.getText().toString().trim());
        hmap.put("habitation", userHabitation.getText().toString().trim());
        hmap.put("gender", userGender.getText().toString().trim());




        t = new TestSender();
        t.doThePost("http://socialmaps.openode.io/api/register",hmap);

        userFirstName.setText("");
        userLastName.setText("");
        userName.setText("");
        userPassword.setText("");
        userEmail.setText("");
        userBirthdate.setText("");
        userHabitation.setText("");
        userGender.setText("");

        checkValidEmail();

    }
    public synchronized void checkValidEmail() {
        while (t.getResp()==null);
        String resp = t.getResp();
        t.resetResp();
        if(resp.contains("token")) {
            Toast.makeText(this, "register succesfull", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
            finish();  //Kill the activity from which you will go to next activity
            startActivity(i);
        } else {
            Toast.makeText(this, "email or username already exist", Toast.LENGTH_SHORT).show();
        }

        }



}
