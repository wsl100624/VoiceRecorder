package ust.voicerecorder;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.*;

import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;

import static android.content.ContentValues.TAG;


public class UploadToHttpServer extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... strings) {
        String url = "http://10.0.1.19/uploadfile.php";
        Log.v(TAG, "postURL: " + url);

        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/voices/audio.3gp";
        Log.v(TAG, "FilePath: " + filePath);


        try {
            // New HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // Post header
            HttpPost httppost = new HttpPost(url);

            File file = new File(filePath);
            FileBody fileBody = new FileBody(file);

            MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
            reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.addPart("file", fileBody);
            httppost.setEntity((HttpEntity) reqEntity);

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {
                String responseStr = EntityUtils.toString(resEntity).trim();
                Log.v(TAG, "Response: " + responseStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
