package ust.voicerecorder;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;


public class UploadWithOkhttp extends AsyncTask<Void, Void, Void>{
    //请求地址
    String requestUrl = "http://140.209.68.84/uploadWithOkhttp.php";

    //创建File
    String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/voices/audio.3gp";
    File file = new File(filePath);

    OkHttpClient mOkHttpClient = new OkHttpClient();

    //创建RequestBody
    RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
    RequestBody requestBody = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addPart(Headers.of(
                    "Content-Disposition",
                    "form-data; name = \"audio\"; filename = \"audio.3gp\""), fileBody)
            .build();

    //创建Request
    final Request request = new Request.Builder()
            .url(requestUrl)
            .post(requestBody)
            .build();

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.e(TAG, "Response -----> Upload Successful!");
            } else {
                Log.e(TAG, "Response -----> Upload Failed!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
