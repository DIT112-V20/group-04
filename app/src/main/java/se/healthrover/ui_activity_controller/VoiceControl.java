package se.healthrover.ui_activity_controller;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import se.healthrover.R;
import se.healthrover.car_service.CarManagement;
import se.healthrover.car_service.CarManagementImp;
import se.healthrover.entities.CarCommands;
import se.healthrover.entities.HealthRoverCar;

public class VoiceControl extends AppCompatActivity {

    private Button manualControlButton;
    private Button guideButton;
    private HealthRoverCar healthRoverCar;
    private String carName;
    private TextView headerVoiceControl;
    private ImageView speechButton;
    private TextView speechToText;
    private int speed = 30;
    private static final int SPEECH_RESULT = 1;
    private static final int VELOCITY_MODIFIER = 10;
    private static final int NEGATION = -1;
    private static final int SPEED_CHECK = 0;
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
        guideButton = findViewById(R.id.guideButton);
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

        guideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflate the layout of the popup window
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.guide_popup, null);

                // Create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // Show the popup window
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                // Dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
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
                if (speed < SPEED_CHECK) {
                    speed = speed * NEGATION;
                }
                carManagement.moveCar(healthRoverCar, speed, Integer.parseInt(CarCommands.NO_ANGLE.getCarCommands()));
                break;
            case "stop":
                carManagement.moveCar(healthRoverCar, Integer.parseInt(CarCommands.NO_MOVEMENT.getCarCommands()), Integer.parseInt(CarCommands.NO_ANGLE.getCarCommands()));
                break;
            case "increase":
                if (speed < Integer.parseInt(CarCommands.VC_MAX_VELOCITY.getCarCommands()) && speed > Integer.parseInt(CarCommands.VC_MIN_VELOCITY.getCarCommands())) {
                    speed += VELOCITY_MODIFIER;
                    carManagement.moveCar(healthRoverCar, speed, Integer.parseInt(CarCommands.NO_ANGLE.getCarCommands()));
                } else {
                    Toast.makeText(VoiceControl.this, "Maximum velocity reached", Toast.LENGTH_SHORT).show();
                }
                break;
            case "decrease":
                if (speed > Integer.parseInt(CarCommands.VC_MIN_VELOCITY.getCarCommands())) {
                    speed -= VELOCITY_MODIFIER;
                    carManagement.moveCar(healthRoverCar, speed, Integer.parseInt(CarCommands.NO_ANGLE.getCarCommands()));
                } else {
                    Toast.makeText(VoiceControl.this, "Minimum velocity reached", Toast.LENGTH_SHORT).show();
                }
                break;
            case "left":
                carManagement.moveCar(healthRoverCar, speed, Integer.parseInt(CarCommands.LEFT_ANGLE.getCarCommands()));
                break;
            case "right":
                carManagement.moveCar(healthRoverCar, speed, Integer.parseInt(CarCommands.RIGHT_ANGLE.getCarCommands()));
                break;
            case "reverse":
                if(speed>SPEED_CHECK) {
                    speed = speed * NEGATION;
                }
                carManagement.moveCar(healthRoverCar, speed, Integer.parseInt(CarCommands.NO_ANGLE.getCarCommands()));
                break;
            default:
                Toast.makeText(VoiceControl.this, "Invalid command", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
