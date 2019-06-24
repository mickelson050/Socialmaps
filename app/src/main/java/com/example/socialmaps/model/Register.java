package com.example.socialmaps.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

public class Register extends AsyncTask<Void,Void,String>{
    Context c;
    String urlAdress;
    EditText voornaamTxt, achternaamTxt, usernameTxt, passwordTxt, geboortedatumTxt, emailTxt;
    String voornaam, achternaam, username, password, geboortedatum, email;
    ProgressDialog pd;

    public Register(Context c, String urlAddress,EditText...editTexts){
        this.c = c;
        this.urlAdress = urlAddress;
        //input
        this.voornaamTxt=editTexts[0];
        this.achternaamTxt=editTexts[1];
        this.usernameTxt=editTexts[2];
        this.passwordTxt=editTexts[3];
        this.geboortedatumTxt=editTexts[4];
        this.emailTxt=editTexts[5];

        voornaam=voornaamTxt.getText().toString();
        achternaam=achternaamTxt.getText().toString();
        username=usernameTxt.getText().toString();
        password=passwordTxt.getText().toString();
        geboortedatum=geboortedatumTxt.getText().toString();
        email=emailTxt.getText().toString();
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(c);
        pd.setTitle("Send");
        pd.setMessage("Sending..Please wait");
        pd.show();
    }
    @Override
    protected String doInBackground(Void... params) {
        return this.send();
    }
    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        pd.dismiss();
        if(response != null)
        {
//SUCCESS
            Toast.makeText(c,response,Toast.LENGTH_LONG).show();
            voornaamTxt.setText("");
            achternaamTxt.setText("");
            usernameTxt.setText("");
            passwordTxt.setText("");
            geboortedatumTxt.setText("");
            emailTxt.setText("");
        }else
        {
//NO SUCCESS
            Toast.makeText(c,"Unsuccessful "+response,Toast.LENGTH_LONG).show();
        }
    }
    private String send(){
        HttpURLConnection con = Connector.connect(urlAdress);
        if (con==null){
            return null;
        }
        try{
            OutputStream os=con.getOutputStream();
//WRITE
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            bw.write(new RegisterPackager(voornaam,achternaam,username,password,geboortedatum,email).packData());
            bw.flush();
//RELEASE RES
            bw.close();
            os.close();
//HAS IT BEEN SUCCESSFUL?
            int responseCode=con.getResponseCode();
            if(responseCode==con.HTTP_OK)
            {
//GET EXACT RESPONSE
                BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuffer response=new StringBuffer();
                String line;
//READ LINE BY LINE
                while ((line=br.readLine()) != null)
                {
                    response.append(line);
                }
//RELEASE RES
                br.close();
                return response.toString();
            }else
            {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

