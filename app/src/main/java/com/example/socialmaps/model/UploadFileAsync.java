package com.example.socialmaps.model;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UploadFileAsync extends AsyncTask<String, Void, String> {

    private static final String TAG = "UploadFileAsync";

    private String fileUri;
    private String user;
    private String lat;
    private String lon;
    private String publi;

    public UploadFileAsync(String fileUri, String user, String lat, String lon, String publi) {
        this.fileUri = fileUri;
        this.user = user;
        this.lat = lat;
        this.lon = lon;
        this.publi = publi;
        Log.v(TAG, "file uri: " + fileUri + " lat: " + lat + " lon: " + lon);
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            String sourceFileUri = fileUri;

            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(sourceFileUri);

            if (sourceFile.isFile()) {

                try {
                    Log.v(TAG, "In if(source is file)");
                    String upLoadServerUri = "http://socialmaps.dx.am/upload_photo.php";

                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(
                            sourceFile);
                    URL url = new URL(upLoadServerUri);

                    // Open a HTTP connection to the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE",
                            "multipart/form-data");
                    conn.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("bill", sourceFileUri);
                    conn.setRequestProperty("user", user);
                    conn.setRequestProperty("lat", lat);
                    conn.setRequestProperty("lon", lon);
                    conn.setRequestProperty("publi", publi);

                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"user\""+lineEnd+lineEnd+ user + lineEnd);
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"lat\""+lineEnd+lineEnd+ lat + lineEnd);
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"lon\""+lineEnd+lineEnd+ lon + lineEnd);
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"publi\""+lineEnd+lineEnd+ publi + lineEnd);
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"bill\";filename=\""
                            + sourceFileUri + "\"" + lineEnd);


                    dos.writeBytes(lineEnd);

                    // create a buffer of maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math
                                .min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0,
                                bufferSize);

                    }

                    // send multipart form data necesssary after file
                    // data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens
                            + lineEnd);

                    // Responses from the server (code and message)
                    //serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn
                            .getResponseMessage();

                    Log.v(TAG,"Server response: " + serverResponseMessage);

//                    if (serverResponseCode == 200) {
//
//                        // messageText.setText(msg);
//                        //Toast.makeText(ctx, "File Upload Complete.",
//                        //      Toast.LENGTH_SHORT).show();
//
//                        // recursiveDelete(mDirectory1);
//
//                    }

                    // close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                } catch (Exception e) {

                    // dialog.dismiss();
                    e.printStackTrace();

                }
                // dialog.dismiss();

            } // End else block


        } catch (Exception ex) {
            // dialog.dismiss();

            ex.printStackTrace();
        }
        return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {

    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}