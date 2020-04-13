package se.healthrover;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ManualControl extends AppCompatActivity {

    private Button forwardButton;
    private Button stopButton;
    private TextView getResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_control);
        forwardButton = findViewById(R.id.forwardButton);
        stopButton = findViewById(R.id.stopButton);
        getResponse = findViewById(R.id.getResponse);


    }

    public void sendGetRequest (String url) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getResponse.setText(response);

            }
        }, new Response.ErrorListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onErrorResponse(VolleyError error) {
                getResponse.setText("Error:\n" + error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void moveForward(View view) {
        sendGetRequest(HealthRoverCar.HEALTH_ROVER_CAR1.getUrl()+"forward");
    }

    public void stopCar(View view) {
        sendGetRequest(HealthRoverCar.HEALTH_ROVER_CAR1.getUrl()+"stop");
    }

}
