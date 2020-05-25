package se.healthrover.ui_activity_controller.voice_control;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.common.base.CharMatcher;

import java.io.InputStream;
import java.util.ArrayList;

import se.healthrover.R;
import se.healthrover.car_service.CarManagement;
import se.healthrover.entities.Car;
import se.healthrover.entities.CarCommands;
import se.healthrover.entities.ObjectFactory;
import se.healthrover.ui_activity_controller.ManualControl;
import se.healthrover.ui_activity_controller.utilities.UserInterfaceUtilities;

public class SpeechRecognition extends AppCompatActivity {

    private Button manualControlButton;
    private Button guideButton;
    private Car healthRoverCar;
    private String carName;
    private TextView headerVoiceControl;
    private ImageView speechButton;
    private SessionName session;
    private SessionsClient sessionsClient;
    private int speed = 30;
    private CarManagement carManagement;
    private UserInterfaceUtilities userInterfaceUtilities;
    private static final int VELOCITY_MODIFIER = 10;
    private static final int NEGATION = -1;
    private static final int SPEED_CHECK = 0;
    private static final int SPEECH_RESULT = 1;
    private static final String UUID = "HealthRover";
    private static final String DIALOGFLOW_RESPONSE_KEY_DIRECTION = "direction";
    private static final String DIALOGFLOW_RESPONSE_KEY_SPEED = "speed";
    private static final String DIALOGFLOW_RESPONSE_KEY_ANGLE = "angle";
    private static final String DIALOGFLOW_DIRECTION_VALUE_FORWARD = "forward";
    private static final String DIALOGFLOW_DIRECTION_VALUE_REVERSE = "reverse";
    private static final String DIALOGFLOW_DIRECTION_VALUE_STOP = "stop";
    private static final String DIALOGFLOW_DIRECTION_VALUE_INCREASE = "increase";
    private static final String DIALOGFLOW_DIRECTION_VALUE_DECREASE = "decrease";
    private static final String DIALOGFLOW_DIRECTION_VALUE_LEFT = "left";
    private static final String DIALOGFLOW_DIRECTION_VALUE_RIGHT = "right";
    private static final String CONTROL_TYPE = "voice";


    //Create the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(ObjectFactory.getInstance().getExceptionHandler(this, healthRoverCar));
        initialize();

    }
    //Refresh the activity
    @Override
    protected void onRestart() {
        super.onRestart();
        Thread.setDefaultUncaughtExceptionHandler(ObjectFactory.getInstance().getExceptionHandler(this, healthRoverCar));
        initialize();
    }

    public SpeechRecognition(){
        carManagement = ObjectFactory.getInstance().getCarManagement();
        userInterfaceUtilities = ObjectFactory.getInstance().getInterfaceUtilities();
    }

    // Using the method to load and initialize the content of the page
    private void initialize() {
        setContentView(R.layout.speech_recognition);
        headerVoiceControl = findViewById(R.id.headerVoiceControl);
        healthRoverCar = (Car) getIntent().getSerializableExtra(getString(R.string.car_name));
        carName = healthRoverCar.getName();
        headerVoiceControl.setText(carName);
        manualControlButton = findViewById(R.id.manualControl);
        guideButton = findViewById(R.id.guideButton);
        speechButton = findViewById(R.id.speechButton);
        connectDialogflow();

        manualControlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ObjectFactory.getInstance().getIntent(SpeechRecognition.this, ManualControl.class);
                intent.putExtra(getString(R.string.car_name), healthRoverCar);
                startActivity(intent);
            }
        });
        guideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInterfaceUtilities.showCustomPopup(SpeechRecognition.this, R.layout.guide_popup, view);
            }
        });

        speechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent speechIntent = ObjectFactory.getInstance().getIntent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.voice_pop_up_message));
                startActivityForResult(speechIntent, SPEECH_RESULT);
            }
        });
    }
    // Using back button to return to Car select page
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = ObjectFactory.getInstance().getIntent(SpeechRecognition.this, ManualControl.class);
        intent.putExtra(getString(R.string.car_name), healthRoverCar);
        carManagement.getCars().clear();
        startActivity(intent);
    }

    // Convert speech to String send the corresponding String to Dialogflow
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == SPEECH_RESULT && resultCode == RESULT_OK){
            assert data != null;
            ArrayList<String> spokenWords = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            assert spokenWords != null;
            sendVoiceCommand(spokenWords.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Method to set up the connection to Dialogflow API
    // using a private access key. Add your own API key under src/res/raw
    private void connectDialogflow(){
        try {
            InputStream stream = getResources().openRawResource(R.raw.dialogflow_access_key);
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
            String projectId = ((ServiceAccountCredentials)credentials).getProjectId();
            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build();
            sessionsClient = SessionsClient.create(sessionsSettings);
            session = SessionName.of(projectId, UUID);

        } catch (Exception e) {
            userInterfaceUtilities.showCustomToast(SpeechRecognition.this, getString(R.string.dialogflow_connection_fail));
        }
    }

    // Send requested query to the Dialogflow API
    private void sendVoiceCommand(String command) {
        QueryInput queryInput = QueryInput.newBuilder().setText(TextInput.newBuilder().setText(command).setLanguageCode("en-US")).build();
        ObjectFactory.getInstance().getRequestTask(SpeechRecognition.this, session, sessionsClient , queryInput).execute();
    }

    // Process the response received from Dialogflow
    public void processResponse(DetectIntentResponse response) {
        if (response != null) {
            try {

                String receivedCommand = response.getQueryResult().getParameters().getFieldsOrThrow(DIALOGFLOW_RESPONSE_KEY_DIRECTION).getStringValue();
                String receivedSpeed = response.getQueryResult().getParameters().getFieldsOrThrow(DIALOGFLOW_RESPONSE_KEY_SPEED).getStringValue();
                String receivedAngle = response.getQueryResult().getParameters().getFieldsOrThrow(DIALOGFLOW_RESPONSE_KEY_ANGLE).getStringValue();
                // Taking only the integer value from the receivedAngle String which includes not used characters
                receivedAngle = CharMatcher.inRange('0', '9').retainFrom(receivedAngle);

                // Here we check for empty values to be able to call driveCarCommand with
                // default values, otherwise it will use user-specified ones
                if(speedValidation(receivedSpeed) && angleValidation(receivedAngle)) {
                    if (!receivedAngle.equals("") && receivedSpeed.equals("")) {
                        driveCarCommand(receivedCommand, speed, Integer.parseInt(receivedAngle));
                    } else if (receivedAngle.equals("") && !receivedSpeed.equals("")) {
                        driveCarCommand(receivedCommand, Integer.parseInt(receivedSpeed), Integer.parseInt(CarCommands.DEFAULT_ANGLE.getCarCommands()));
                    } else if (receivedAngle.equals("") && receivedSpeed.equals("")) {
                        driveCarCommand(receivedCommand, speed, Integer.parseInt(CarCommands.DEFAULT_ANGLE.getCarCommands()));
                    } else {
                        driveCarCommand(receivedCommand, Integer.parseInt(receivedSpeed), Integer.parseInt(receivedAngle));
                    }
                }else{
                    userInterfaceUtilities.showCustomToast(SpeechRecognition.this, getString(R.string.dialogflow_response_out_of_limits));
                }
            }catch (IllegalArgumentException e){
                userInterfaceUtilities.showCustomToast(SpeechRecognition.this, getString(R.string.dialogflow_no_correlation_matched));
            }
        } else {
            userInterfaceUtilities.showCustomToast(SpeechRecognition.this, getString(R.string.dialogflow_connection_issue));
        }
    }

    // Method to validate that requested speed is always within boundaries
    private Boolean speedValidation(String speed) {
        if(speed.equals("")){
            return true;
        }else {
            return Integer.parseInt(speed) <= Integer.parseInt(CarCommands.VC_MAX_VELOCITY.getCarCommands());
        }
    }

    // Method to validate that the requested angle is always within boundaries
    private Boolean angleValidation(String angle){
        if(angle.equals("")){
            return true;
        }else{
            return Integer.parseInt(angle) <= Integer.parseInt(CarCommands.DEFAULT_ANGLE.getCarCommands()) &&
                    Integer.parseInt(angle) >= (Integer.parseInt(CarCommands.DEFAULT_ANGLE.getCarCommands())*NEGATION);
        }
    }

    private void driveCarCommand(String command, int receivedSpeed, int receivedAngle) {
        switch (command) {
            case DIALOGFLOW_DIRECTION_VALUE_FORWARD:
                speed = receivedSpeed;
                if (speed < SPEED_CHECK) {
                    speed = speed * NEGATION;
                }
                carManagement.moveCar(healthRoverCar, speed, Integer.parseInt(CarCommands.NO_ANGLE.getCarCommands()), CONTROL_TYPE, this);
                break;
            case DIALOGFLOW_DIRECTION_VALUE_STOP:
                carManagement.moveCar(healthRoverCar, Integer.parseInt(CarCommands.NO_MOVEMENT.getCarCommands()), Integer.parseInt(CarCommands.NO_ANGLE.getCarCommands()), CONTROL_TYPE, this);
                break;
            case DIALOGFLOW_DIRECTION_VALUE_INCREASE:
                if (speed < Integer.parseInt(CarCommands.VC_MAX_VELOCITY.getCarCommands()) && speed > Integer.parseInt(CarCommands.VC_MIN_VELOCITY.getCarCommands())) {
                    speed += VELOCITY_MODIFIER;
                    carManagement.moveCar(healthRoverCar, speed, Integer.parseInt(CarCommands.NO_ANGLE.getCarCommands()), CONTROL_TYPE, this);
                } else {
                    userInterfaceUtilities.showCustomToast(SpeechRecognition.this, getString(R.string.voice_control_max_velocity));
                }
                break;
            case DIALOGFLOW_DIRECTION_VALUE_DECREASE:
                if (speed > Integer.parseInt(CarCommands.VC_MIN_VELOCITY.getCarCommands())) {
                    speed -= VELOCITY_MODIFIER;
                    carManagement.moveCar(healthRoverCar, speed, Integer.parseInt(CarCommands.NO_ANGLE.getCarCommands()), CONTROL_TYPE, this);
                } else {
                    userInterfaceUtilities.showCustomToast(SpeechRecognition.this, getString(R.string.voice_control_min_velocity));
                }
                break;
            case DIALOGFLOW_DIRECTION_VALUE_LEFT:
                speed = receivedSpeed;
                carManagement.moveCar(healthRoverCar, speed, (receivedAngle * NEGATION), CONTROL_TYPE, this);
                break;
            case DIALOGFLOW_DIRECTION_VALUE_RIGHT:
                speed = receivedSpeed;
                carManagement.moveCar(healthRoverCar, speed, receivedAngle, CONTROL_TYPE, this);
                break;
            case DIALOGFLOW_DIRECTION_VALUE_REVERSE:
                speed = receivedSpeed;
                if(speed>SPEED_CHECK) {
                    speed = speed * NEGATION;
                }
                carManagement.moveCar(healthRoverCar, speed, Integer.parseInt(CarCommands.NO_ANGLE.getCarCommands()), CONTROL_TYPE, this);
                break;
            default:
                userInterfaceUtilities.showCustomToast(SpeechRecognition.this, getString(R.string.invalid_command));
                break;
        }
    }

}

