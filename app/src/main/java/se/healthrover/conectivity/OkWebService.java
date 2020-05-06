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

public class OkWebService implements HealthRoverWebService {


    private Activity activity;
    private OkHttpClient client = new OkHttpClient();

    public OkWebService(Activity activity){
        this.activity = activity;
    }


    @Override
    public void createWebSocket(String url, final Activity activity) {
        //Builds a GET request to a given url
        Request request = new Request.Builder()
                .url(url)
                .build();
        //Used to control async threading in java, we need to wait for the request to execute in order to continue

        //enqueue the request and run it on a thread, Logging the failures into the log and on success setting the status to true, using countdown to manage threading
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("Error","Failed to connect: "+e.getMessage());

                    }
                });

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response)  {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(response.toString());
                        Log.i("Success","Success: "+response.code());

                    }
                });


            }});

    }
}
