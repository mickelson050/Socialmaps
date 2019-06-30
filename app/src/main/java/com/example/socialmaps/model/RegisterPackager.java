package com.example.socialmaps.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

public class RegisterPackager {

    String username, voornaam, achternaam, geboortedatum, email, wachtwoord;

    public RegisterPackager(String username, String voornaam, String achternaam, String geboortedatum, String email, String wachtwoord) {
        this.username = username;
        this.voornaam = voornaam;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
        this.email = email;
        this.wachtwoord = wachtwoord;
    }

    public String packData(){
        JSONObject jo = new JSONObject();
        StringBuffer packedData = new StringBuffer();

        try{
            jo.put("voornaam", voornaam);
            jo.put("achternaam", achternaam);
            jo.put("username", username);
            jo.put("password", wachtwoord);
            jo.put("geboortedatum", geboortedatum);
            jo.put("email", email);
            boolean firstValue = true;
            Iterator it = jo.keys();
            do {
                String key=it.next().toString();
                String value=jo.get(key).toString();

                if(firstValue)
                {
                    firstValue=false;
                }else
                {
                    packedData.append("&");
                }

                packedData.append(URLEncoder.encode(key,"UTF-8"));
                packedData.append("=");
                packedData.append(URLEncoder.encode(value,"UTF-8"));
            }while (it.hasNext());

            return packedData.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
