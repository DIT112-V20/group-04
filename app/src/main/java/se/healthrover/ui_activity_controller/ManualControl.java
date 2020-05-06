package se.healthrover.ui_activity_controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import io.github.controlwear.virtual.joystick.android.JoystickView;
import se.healthrover.R;
import se.healthrover.car_service.CarManagement;
import se.healthrover.car_service.CarManagementImp;
import se.healthrover.entities.HealthRoverCar;


public class ManualControl extends AppCompatActivity {
   private static final int REQUEST_DELAY = 100;

    private TextView header;
    private String carName;
    private TextView angleText;
    private TextView strengthText;
    private TextView textSpeedHeader;
    private TextView textAngleHeader;
    private CarManagement carManagement = new CarManagementImp();
    private int speed;
    private int turningAngle;
    private Button voiceControl;
    private Boolean statusCheck;
    private HealthRoverCar healthRoverCar;
    private long lastRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();

    }
    //On restart refresh the content
    @Override
    protected void onRestart() {
        super.onRestart();
        initialize();
    }

    // Using the method to load and initialize the content
    private void initialize(){

        setContentView(R.layout.manual_control);
        header = findViewById(R.id.header);
        voiceControl = findViewById(R.id.voiceControl);
        carName = getIntent().getStringExtra("carName");
        header.setText(carName);
        angleText = findViewById(R.id.textView_angle);
        strengthText = findViewById(R.id.textView_strength);
        healthRoverCar = HealthRoverCar.valueOf(HealthRoverCar.getCarObjectName(carName));
        lastRequest = 0;

        final JoystickView joystickController = findViewById(R.id.joystick);

        joystickController.setOnMoveListener(new JoystickView.OnMoveListener() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onMove(int angle, int strength) {
                //Update ui text
                speed = convertSpeed(strength, angle);
                turningAngle = convertAngle(angle);
                angleText.setText(turningAngle + "° angle");
                strengthText.setText(speed + "% speed");

                //Send request to move the car, but only if REQUEST_DELAY ms have passed since last request sent
                if (SystemClock.currentThreadTimeMillis() - lastRequest > REQUEST_DELAY) {
                    carManagement.moveCar(healthRoverCar, speed, turningAngle);
                    lastRequest = SystemClock.currentThreadTimeMillis();
                }

                //checkRequest(healthRoverCar, speed, turningAngle); TODO implement methods bellow
            }
        });


        voiceControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManualControl.this, VoiceControl.class);
                intent.putExtra("carName", healthRoverCar.getCarName());
                startActivity(intent);
            }
        });


    }
    /* TODO implement error handling for HTTP
    private void switchCarSelect(String errorMessage){
        Intent intent = new Intent(ManualControl.this, CarSelect.class);
        startActivity(intent);
        Toast.makeText(ManualControl.this, errorMessage, Toast.LENGTH_LONG).show();
    }


    private void checkRequest(HealthRoverCar car, int speed, int turningAngle){
        statusCheck = carManagement.moveCar(car, speed, turningAngle);
        if(!statusCheck){
            if(!carManagement.checkStatus(car)){
                switchCarSelect("Connection to car was lost!");
            }else{
                // Allow the application to try to send an http request 3 times
                for(int i = 0; i<3; i++) {
                    Toast.makeText(ManualControl.this, "Trying to connect to the car", Toast.LENGTH_SHORT).show();
                    statusCheck = carManagement.moveCar(car, speed, turningAngle);
                    if(statusCheck){
                        return;
                    }else if(i == 2){
                        switchCarSelect("Request timed out");
                    }
                    try {
                        wait(500);
                    }catch (Exception e){
                        Log.i("Error","Failed to connect: "+e.getMessage());
                    }
                }
            }
        }
    }
*/
    // Using back button to return to Car select page
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ManualControl.this, CarSelect.class);
        startActivity(intent);
    }

    //Convert strength from joystick to the corresponding car speed
    private int convertSpeed(int strength, int angle) {
        if (angle > 180) {
            return - strength;
        } else {
            return strength;
        }
    }

    //Converts angle from joystick to the corresponding car angles
    private int convertAngle(int angle) {
        if (angle <= 90 && angle >= 0) {
            return (90 - angle);
        } else if (angle > 90 && angle <= 180) {
            return (angle - 90) * (-1);
        } else if (angle > 180 && angle <= 270) {
            return (angle - 270);
        } else {
            return (angle - 270);
        }
    }
}
