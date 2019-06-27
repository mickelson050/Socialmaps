package com.example.socialmaps.model;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


public class TestSender {

    private static final String TAG = "TestSender";


    private String URL;
    private HashMap<String, String> values;

    private String resp;

    public void doThePost(String URL, HashMap<String, String> values) {
        this.URL = URL;
        this.values = values;

        new PostDataAsyncTask().execute();
    }

    public String getResp() {
        return resp;
    }

    public class PostDataAsyncTask extends AsyncTask<String, String, String> {



        protected void onPreExecute() {
            super.onPreExecute();
            // do stuff before posting data
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                // 1 = post text data, 2 = post file
                int actionChoice = 1;

                // post a text data
                if(actionChoice==1){
                    postText();
                }

                // post a file
                else{
                    //postFile();
                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String lenghtOfFile) {
            // do stuff after posting data
        }
    }

    public void postText(){
        try{
            // url where the data will be posted
            String postReceiverUrl = URL;
            Log.v(TAG, "postURL: " + postReceiverUrl);

            // HttpClient
            HttpClient httpClient = new DefaultHttpClient();

            // post header
            HttpPost httpPost = new HttpPost(postReceiverUrl);

            // add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            JSONObject sendValues = new JSONObject(values);

            // Going trough Hashmap and adding to List
            Set set = values.entrySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()) {
                Map.Entry mentry = (Map.Entry)iterator.next();
                nameValuePairs.add(new BasicNameValuePair((String)mentry.getKey(), (String)mentry.getValue()));
            }

            String sendThis = sendValues.toString();

            StringEntity requestEntity = new StringEntity(sendThis);

            httpPost.setEntity(requestEntity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // execute HTTP post request
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {

                String responseStr = EntityUtils.toString(resEntity).trim();
                Log.v(TAG, "Response: " +  responseStr);
                resp = responseStr;

                // you can add an if statement here and do other actions based on the response
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
