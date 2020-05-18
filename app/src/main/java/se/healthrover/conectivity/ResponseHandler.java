package se.healthrover.conectivity;

import android.app.Activity;
import se.healthrover.R;
import se.healthrover.entities.ObjectFactory;
import se.healthrover.ui_activity_controller.UserInterfaceUtilities;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.tts.*;

import java.util.Locale;

public class ResponseHandler {

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

    private void makeSpeaker(Activity activity, String spokenString){
        speaker = new TextToSpeech(activity.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    speaker.setLanguage(Locale.ENGLISH);
                }
            }
        });
        speaker.speak(spokenString, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void handleFailure(String string, Activity activity){
        makeVibrator(activity);
        userInterfaceUtilities.showCustomToast(activity, activity.getString(R.string.car_is_offline));
        makeSpeaker(activity, activity.getString(R.string.car_is_offline));
    }

    public void handleSuccess(String string){}

    private void handleObstacleDetection(){}
}
