package se.healthrover.ui_activity_controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import io.github.controlwear.virtual.joystick.android.JoystickView;
import se.healthrover.R;
import se.healthrover.car_service.CarManagement;
import se.healthrover.car_service.CarManagementImp;
import se.healthrover.entities.HealthRoverCar;


public class ManualControl extends AppCompatActivity {


    private TextView header;
    private String carName;
    private TextView angleText;
    private TextView strengthText;
    private TextView coordinatesText;
    private CarManagement carManagement = new CarManagementImp();
    private int speed;
    private int turningAngle;
    private Button voiceControl;
    private TextToSpeech textToSpeech;
    private Boolean statusCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_control);
        header = findViewById(R.id.header);
        voiceControl = findViewById(R.id.voiceControl);
        carName = getIntent().getStringExtra("carName");
        header.setText(carName);
        angleText = (TextView) findViewById(R.id.textView_angle);
        strengthText = (TextView) findViewById(R.id.textView_strength);
        coordinatesText = findViewById(R.id.textView_coordinate);



        final JoystickView joystickController = (JoystickView) findViewById(R.id.joystick);

        joystickController.setOnMoveListener(new JoystickView.OnMoveListener() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onMove(int angle, int strength) {
                HealthRoverCar tempCar = HealthRoverCar.valueOf(HealthRoverCar.getCarObjectName(carName));
                speed = convertSpeed(strength, angle);
                turningAngle = convertAngle(angle);
                angleText.setText(turningAngle + "° angle");
                strengthText.setText(speed + "% speed");
                carManagement.moveCar(tempCar, speed, turningAngle);
                checkRequest(tempCar, speed, turningAngle);
                coordinatesText.setText(
                        String.format("x%03d:y%03d",
                                joystickController.getNormalizedX(),
                                joystickController.getNormalizedY())
                );
            }
        });

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });
        voiceControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.speak("This option is not available yet",TextToSpeech.QUEUE_FLUSH,null);
            }
        });


    }
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
