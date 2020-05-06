package se.healthrover.conectivity;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import se.healthrover.ui_activity_controller.VoiceControl;

public class VolleyWebService implements HealthRoverWebService {

    private Activity activity;

    public VolleyWebService(Activity activity){
        this.activity = activity;
    }



    @Override
    public void createWebSocket(String url, final Activity activity) {


        RequestQueue queue = Volley.newRequestQueue(activity);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println(response.toString());
                                Log.i("Success","Success: "+response.toString());
                            }
                        });
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


}
