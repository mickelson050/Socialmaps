package com.example.socialmaps.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * 1.BASICALLY PACKS DATA WE WANNA SEND
 */
public class DataPackager {

    String user, content, publi;
    Double lat, lon;

    /*
    SECTION 1.RECEIVE ALL DATA WE WANNA SEND
     */
    public DataPackager(String user, Double lat, Double lon, String content, String publi) {
        this.user = user;
        this.lat = lat;
        this.lon = lon;
        this.content = content;
        this.publi = publi;
    }

    /*
   SECTION 2
   1.PACK THEM INTO A JSON OBJECT
   1. READ ALL THIS DATA AND ENCODE IT INTO A FROMAT THAT CAN BE SENT VIA NETWORK
    */
    public String packData()
    {
        JSONObject jo=new JSONObject();
        StringBuffer packedData=new StringBuffer();

        try
        {
            jo.put("User",user);
            jo.put("Lat",lat);
            jo.put("Lon",lon);
            jo.put("Content",content);
            jo.put("Public",publi);

            Boolean firstValue=true;

            Iterator it=jo.keys();

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