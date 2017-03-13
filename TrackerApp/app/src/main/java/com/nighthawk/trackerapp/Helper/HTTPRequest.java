package com.nighthawk.trackerapp.Helper;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Michael on 12/3/17.
 */

public class HTTPRequest extends AsyncTask<String, Void, Void>{

    @Override
    protected Void doInBackground(String... data){
        String dataToBeSend = data[0].toString();
        String tokenToBeSend = data[1];
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("DATA",dataToBeSend)
                .add("Tokens",tokenToBeSend)
                .build();
        Request request = new Request.Builder()
                .url("http://172.21.148.167/TrackerApplication/push_notification.php")
                .post(body)
                .build();
        try{
            Response response = client.newCall(request).execute();
            Log.d("Response",response.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
