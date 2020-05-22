package se.healthrover.conectivity;

import android.app.Activity;
import se.healthrover.R;
import se.healthrover.entities.Car;
import se.healthrover.entities.ObjectFactory;
import se.healthrover.ui_activity_controller.ManualControl;
import se.healthrover.ui_activity_controller.utilities.UserInterfaceUtilities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.*;

import java.util.Locale;

public class ResponseHandler {

    private static final String HTTP_OBSTACLE_RESPONSE = "obstacle";
    private static final String HTTP_STATUS_RESPONSE = "status";
    private UserInterfaceUtilities userInterfaceUtilities;
    private Vibrator vibrator;
    private TextToSpeech speaker;
    private final int VIBRATION_TIME = 500;

    public ResponseHandler(){
        userInterfaceUtilities = ObjectFactory.getInstance().getInterfaceUtilities();
    }

    private void makeVibrator(Activity activity){
        vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(VIBRATION_TIME, VibrationEffect.DEFAULT_AMPLITUDE));
        }else {
            vibrator.vibrate(VIBRATION_TIME);
        }
    }

    private void makeSpeaker(final Activity activity, final String spokenString){
        speaker = new TextToSpeech(activity.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    speaker.setLanguage(Locale.ENGLISH);
                    speaker.speak(spokenString, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }

    // Method to handle connection failure to the SmartCar
    // Currently only handling status failure connection
    public void handleFailure(final Activity activity, final Car healthRoverCar){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                userInterfaceUtilities.showCustomToast(activity, activity.getString(R.string.connection_failure) + healthRoverCar.getName());
                makeVibrator(activity);
                makeSpeaker(activity, activity.getString(R.string.connection_failure) + healthRoverCar.getName());
            }
        });
    }

    // Method to handle successful responses from the server that pass different parameters
    public void handleSuccess(final String responseData, final Activity activity, final Car healthRoverCar){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (responseData.equals(HTTP_STATUS_RESPONSE)) {
                    handleSuccessStatus(activity, healthRoverCar);
                }
                else if(responseData.equals(HTTP_OBSTACLE_RESPONSE)){
                    handleObstacleDetection(activity);
                }
            }
        });
    }

    // If status request is successful the end-user will be notified with toast, vibration and speech
    private void handleObstacleDetection(Activity activity){
        userInterfaceUtilities.showCustomToast(activity, activity.getString(R.string.obstacle_detection));
        makeVibrator(activity);
        makeSpeaker(activity, activity.getString(R.string.obstacle_detection));
    }

    // If status request is successful the manual control page is loaded and the car name is passed as a parameter
    private void handleSuccessStatus(Activity activity, Car healthRoverCar){
        Intent intent = ObjectFactory.getInstance().getIntent(activity, ManualControl.class);
        intent.putExtra(activity.getString(R.string.car_name), healthRoverCar.getName());
        activity.startActivity(intent);
        makeSpeaker(activity, activity.getString(R.string.connection_success) + healthRoverCar.getName());
    }
}
