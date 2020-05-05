package se.healthrover.conectivity;

import android.app.Activity;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HttpService {

    private boolean checkStatus = false;
    private OkHttpClient client = new OkHttpClient();
    private final int HTTP_SUCCESS = 200;

    public boolean sendGetRequest (String url){

        //Builds a GET request to a given url
        Request request = new Request.Builder()
                .url(url)
                .build();
        //Used to control async threading in java, we need to wait for the request to execute in order to continue
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        //enqueue the request and run it on a thread, Logging the failures into the log and on success setting the status to true, using countdown to manage threading
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                checkStatus = false;
                Log.i("Error","Failed to connect: "+e.getMessage());

                    countDownLatch.countDown();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)  {
                Log.i("Success","Success: "+response.code());
                if (response.code() == HTTP_SUCCESS) {
                    checkStatus = true;
                }
                else {
                    checkStatus = false;
                }
                    countDownLatch.countDown();

        }});

            //Wait for the request thread to finish before returning the result
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        return checkStatus;
    }

}
