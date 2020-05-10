package se.healthrover.ui_activity_controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import io.github.controlwear.virtual.joystick.android.JoystickView;
import se.healthrover.R;
import se.healthrover.car_service.CarManagement;
import se.healthrover.car_service.CarManagementImp;
import se.healthrover.entities.HealthRoverCar;
import se.healthrover.entities.HealthRoverJoystick;
import se.healthrover.ui_activity_controller.voice_control.SpeechRecognition;


public class ManualControl extends AppCompatActivity {

    private TextView header;
    private String carName;
    private TextView angleText;
    private TextView strengthText;
    private CarManagement carManagement;
    private TextView textSpeedHeader;
    private TextView textAngleHeader;
    private int speed;
    private int turningAngle;
    private Button voiceControl;
    private HealthRoverCar healthRoverCar;
    private HealthRoverJoystick healthRoverJoystick;

    private int[] lastSpeedAndAngleValues;

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

    public ManualControl(){
        carManagement = new CarManagementImp();
    }

    // Using the method to load and initialize the content
    private void initialize(){
        setContentView(R.layout.manual_control);
        healthRoverJoystick = new HealthRoverJoystick(this);
        header = findViewById(R.id.header);
        voiceControl = findViewById(R.id.voiceControl);
        carName = getIntent().getStringExtra("carName");
        header.setText(carName);
        angleText = findViewById(R.id.textView_angle);
        strengthText = findViewById(R.id.textView_strength);
        healthRoverCar = HealthRoverCar.valueOf(HealthRoverCar.getCarObjectNameByCarName(carName));
        lastSpeedAndAngleValues = new int[]{0, 0};

        final JoystickView joystickController = findViewById(R.id.joystick);

        joystickController.setOnMoveListener(new HealthRoverJoystick.OnMoveListener() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onMove(int angle, int strength) {
                //Calculate the angle and speeed
                speed = healthRoverJoystick.convertSpeed(strength, angle);
                turningAngle = healthRoverJoystick.convertAngle(angle);
                //If the speed and angle has changed, needed to reduce the number of request send to the server
                if(lastSpeedAndAngleValues[0] != speed || lastSpeedAndAngleValues[1] != turningAngle){
                    carManagement.moveCar(healthRoverCar, speed, turningAngle, ManualControl.this);
                    lastSpeedAndAngleValues[0] = speed;
                    lastSpeedAndAngleValues[1] = turningAngle;
                }
                //if the joystick button is in the middle(x=50 and y=50) reset the speed
                if (joystickController.getNormalizedX() == 50 && joystickController.getNormalizedY() == 50){
                    speed = 0;
                    turningAngle = 0;
                    carManagement.moveCar(healthRoverCar, speed, turningAngle, ManualControl.this);
                }
                //Update the UI speed and angle
                angleText.setText(turningAngle + "°");
                strengthText.setText(speed + "%");
                //checkRequest(healthRoverCar, speed, turningAngle); TODO implement methods bellow
            }
        });


        voiceControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManualControl.this, SpeechRecognition.class);
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

}
