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
import se.healthrover.ui_activity_controller.error_handling.ActivityExceptionHandler;
import se.healthrover.ui_activity_controller.voice_control.SpeechRecognition;


public class ManualControl extends AppCompatActivity {

    private TextView header;
    private String carName;
    private TextView angleText;
    private TextView strengthText;
    private CarManagement carManagement;
    private int speed;
    private int turningAngle;
    private Button voiceControl;
    private HealthRoverCar healthRoverCar;
    private HealthRoverJoystick healthRoverJoystick;
    private static final int JOYSTICK_CENTER = 50;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ActivityExceptionHandler(this, healthRoverCar));
        initialize();

    }
    //On restart refresh the content
    @Override
    protected void onRestart() {
        super.onRestart();
        Thread.setDefaultUncaughtExceptionHandler(new ActivityExceptionHandler(this, healthRoverCar));
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
        carName = getIntent().getStringExtra(getString(R.string.carName));
        header.setText(carName);
        angleText = findViewById(R.id.textView_angle);
        strengthText = findViewById(R.id.textView_strength);
        healthRoverCar = HealthRoverCar.valueOf(HealthRoverCar.getCarObjectNameByCarName(carName));

        final JoystickView joystickController = findViewById(R.id.joystick);

        joystickController.setOnMoveListener(new HealthRoverJoystick.OnMoveListener() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onMove(int angle, int strength) {
                //Calculate the angle and speeed
                speed = healthRoverJoystick.convertSpeed(strength, angle);
                turningAngle = healthRoverJoystick.convertAngle(angle);
                //if the joystick button is in the middle(x=50 and y=50) reset the speed
                if (joystickController.getNormalizedX() == JOYSTICK_CENTER && joystickController.getNormalizedY() == JOYSTICK_CENTER){
                    speed = 0;
                    turningAngle = 0;
                    carManagement.moveCar(healthRoverCar, speed, turningAngle, ManualControl.this);
                }
                //Update the UI speed and angle
                angleText.setText(turningAngle + getString(R.string.degreeSymbol));
                strengthText.setText(speed + getString(R.string.percentageSign));
            }
        });

        //Change activity to voice control
        voiceControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManualControl.this, SpeechRecognition.class);
                intent.putExtra(getString(R.string.carName), healthRoverCar.getCarName());
                startActivity(intent);
            }
        });


    }

    // Using back button to return to Car select page
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ManualControl.this, CarSelect.class);
        startActivity(intent);
    }

}
