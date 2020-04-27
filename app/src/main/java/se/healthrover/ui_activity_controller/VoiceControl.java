package se.healthrover.ui_activity_controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import se.healthrover.R;
import se.healthrover.entities.HealthRoverCar;

public class VoiceControl extends AppCompatActivity {

    private Button manualControlButton;
    private HealthRoverCar healthRoverCar;
    private String carName;
    private TextView headerVoiceControl;


    //Create the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();

    }
    //Refresh the activity
    @Override
    protected void onRestart() {
        super.onRestart();
        initialize();
    }
    // Using the method to load and initialize the content of the page
    private void initialize() {

        setContentView(R.layout.voice_control);
        headerVoiceControl = findViewById(R.id.headerVoiceControl);
        carName = getIntent().getStringExtra("carName");
        headerVoiceControl.setText(carName);
        healthRoverCar = HealthRoverCar.valueOf(HealthRoverCar.getCarObjectName(carName));
        manualControlButton = findViewById(R.id.manualControl);

        manualControlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VoiceControl.this, ManualControl.class);
                intent.putExtra("carName", healthRoverCar.getCarName());
                startActivity(intent);
            }
        });
    }

    // Using back button to return to Car select page
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(VoiceControl.this, CarSelect.class);
        startActivity(intent);
    }
}
