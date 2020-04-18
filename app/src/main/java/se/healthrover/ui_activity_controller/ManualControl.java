package se.healthrover.ui_activity_controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import io.github.controlwear.virtual.joystick.android.JoystickView;
import se.healthrover.R;
import se.healthrover.car_service.CarManagement;
import se.healthrover.car_service.CarManagementImp;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_control);
        header = findViewById(R.id.header);
        voiceControl = findViewById(R.id.voiceControl);
        carName = getIntent().getStringExtra("carName");
        //header.setText(carName);
        angleText = (TextView) findViewById(R.id.textView_angle);
        strengthText = (TextView) findViewById(R.id.textView_strength);
        coordinatesText = findViewById(R.id.textView_coordinate);



        final JoystickView joystickController = (JoystickView) findViewById(R.id.joystick);

        joystickController.setOnMoveListener(new JoystickView.OnMoveListener() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onMove(int angle, int strength) {
                speed = convertSpeed(strength, angle);
                turningAngle = convertAngle(angle);
                angleText.setText(turningAngle + "° angle");
                strengthText.setText(speed + "% speed");
                //carManagement.moveCar(HealthRoverCar.valueOf(HealthRoverCar.getCarObjectName(carName)),speed, turningAngle);
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
