package se.healthrover.conectivity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import se.healthrover.R;
import se.healthrover.entities.HealthRoverCar;
import se.healthrover.ui_activity_controller.ManualControl;

public class OkHttpWebService implements HealthRoverWebService {


    private OkHttpClient client;
    private String responseData;
    private static final String HTTP_STATUS_RESPONSE = "status";


    public OkHttpWebService(){
        client = new OkHttpClient();
    }


    @Override
    public void createHttpRequest(final String url, final Activity activity) {
        //Builds a GET request to a given url
        final Request request = new Request.Builder()
                .url(url)
                .build();

        //enqueue the request and run it on a thread, Logging the failures into the log and on success handling the response depending of the response body
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull final Call call, @NotNull final IOException e) {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //If the status request fails a message is displayed in the application
                        if (url.contains(HTTP_STATUS_RESPONSE)){
                            Toast.makeText(activity, activity.getString(R.string.carIsOffline), Toast.LENGTH_LONG).show();
                        }
                        Log.i(activity.getString(R.string.logTitleError),activity.getString(R.string.logConnectionFail) + e.getMessage());
                        client.dispatcher().cancelAll();
                    }
                });

            }
            @Override
            public void onResponse(@NotNull final Call call, @NotNull final Response response) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(response.isSuccessful()) {
                            try {
                                responseData = response.body().string();
                                Log.i(activity.getString(R.string.logSuccess), activity.getString(R.string.logSuccess) + response.code());
                                //If status request is successful the manual control page is loaded and the car name is passed as a parameter
                                if (responseData.equals(HTTP_STATUS_RESPONSE)) {
                                    Intent intent = new Intent(activity, ManualControl.class);
                                    intent.putExtra(activity.getString(R.string.carName), HealthRoverCar.getCarNameByUrl(url.substring(0, 20)));
                                    activity.startActivity(intent);
                                }
                            } catch (IOException e) {
                                Log.i(activity.getString(R.string.logTitleError), activity.getString(R.string.logTitleError) + e.getMessage());
                                client.dispatcher().cancelAll();
                            }
                        }
                        else {
                            client.dispatcher().cancelAll();
                        }

                    }
                });
            }});


    }
}
