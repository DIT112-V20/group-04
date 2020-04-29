package se.healthrover.ui_activity_controller;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import se.healthrover.R;
import se.healthrover.car_service.CarManagement;
import se.healthrover.car_service.CarManagementImp;
import se.healthrover.entities.HealthRoverCar;

public class VoiceControl extends AppCompatActivity {

    private Button manualControlButton;
    private HealthRoverCar healthRoverCar;
    private String carName;
    private TextView headerVoiceControl;
    private ImageView speechButton;
    private TextView speechToText;
    private int speed = 30;
    private static final int NO_ANGLE = 0;
    private static final int STOP = 0;
    private static final int LEFT_ANGLE = -90;
    private static final int RIGHT_ANGLE = 90;
    private static final int SPEECH_RESULT = 1;
    private static final int MAX_VELOCITY = 50;
    private static final int MIN_VELOCITY = 10;
    private static final int VELOCITY_MODIFIER = 10;
    private static final int NEGATION = -1;
    private CarManagement carManagement = new CarManagementImp();


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
        speechButton = findViewById(R.id.speechButton);
        speechToText = findViewById(R.id.speechToText);

        manualControlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VoiceControl.this, ManualControl.class);
                intent.putExtra("carName", healthRoverCar.getCarName());
                startActivity(intent);
            }
        });
        // Method for speech-to-text functionality
        speechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Listening...");
                startActivityForResult(speechIntent, SPEECH_RESULT);
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
    // Convert speech to String
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == SPEECH_RESULT && resultCode == RESULT_OK){
            ArrayList<String> spokenWords = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            speechToText.setText(spokenWords.get(0));
            sendVoiceCommand(spokenWords.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    // Method to send voice commands to the SmartCar
    private void sendVoiceCommand(String command) {
        switch (command) {
            case "forward":
                if (speed < 0) {
                    speed = speed * NEGATION;
                }
                carManagement.moveCar(healthRoverCar, speed, NO_ANGLE);
                break;
            case "stop":
                carManagement.moveCar(healthRoverCar, STOP, NO_ANGLE);
                break;
            case "increase":
                if (speed < MAX_VELOCITY && speed > MIN_VELOCITY) {
                    speed += VELOCITY_MODIFIER;
                    carManagement.moveCar(healthRoverCar, speed, NO_ANGLE);
                } else {
                    Toast.makeText(VoiceControl.this, "Maximum velocity reached", Toast.LENGTH_SHORT).show();
                }
                break;
            case "decrease":
                if (speed > MIN_VELOCITY) {
                    speed -= VELOCITY_MODIFIER;
                    carManagement.moveCar(healthRoverCar, speed, NO_ANGLE);
                } else {
                    Toast.makeText(VoiceControl.this, "Minimum velocity reached", Toast.LENGTH_SHORT).show();
                }
                break;
            case "left":
                carManagement.moveCar(healthRoverCar, speed, LEFT_ANGLE);
                break;
            case "right":
                carManagement.moveCar(healthRoverCar, speed, RIGHT_ANGLE);
                break;
            case "reverse":
                speed = speed * NEGATION;
                carManagement.moveCar(healthRoverCar, speed, NO_ANGLE);
                break;
            default:
                Toast.makeText(VoiceControl.this, "Invalid command", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
