package com.example.socialmaps.model;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class PhotoSender {

    private static final String TAG = "PhotoSender";

    public void sendStuff() {
        try {
            // url where the data will be posted
            String postReceiverUrl = "http://socialmaps.dx.am/new_text_post.php";
            Log.v(TAG, "postURL: " + postReceiverUrl);

// HttpClient
            HttpClient httpClient = new DefaultHttpClient();

// post header
            HttpPost httpPost = new HttpPost(postReceiverUrl);

// add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("firstname", "Mike"));
            nameValuePairs.add(new BasicNameValuePair("lastname", "Dalisay"));
            nameValuePairs.add(new BasicNameValuePair("email", "mike@testmail.com"));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

// execute HTTP post request
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {

                String responseStr = EntityUtils.toString(resEntity).trim();
                Log.v(TAG, "Response: " +  responseStr);

                // you can add an if statement here and do other actions based on the response
            }

        } catch (Exception e) {

        }
    }

}
