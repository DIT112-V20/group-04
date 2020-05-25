package se.healthrover.ui_activity_controller.utilities;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import se.healthrover.R;
import se.healthrover.car_service.CarManagement;
import se.healthrover.entities.Car;
import se.healthrover.entities.CarCommands;
import se.healthrover.entities.ObjectFactory;
import se.healthrover.ui_activity_controller.car_selection.CarSelect;

public class ActivityExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Activity activity;
    private CarManagement carManagement;
    private Car healthRoverCar;
    private static final int RESTART_TIME_OUT = 2;

    public ActivityExceptionHandler(Activity activity, Car healthRoverCar) {
        this.activity = activity;
        carManagement = ObjectFactory.getInstance().getCarManagement();
        this.healthRoverCar = healthRoverCar;
    }
    // The method handles runtime exceptions
    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        Log.i(activity.getString(R.string.log_title_error), activity.getString(R.string.log_crash_title) + e.getMessage());
        Intent intent = ObjectFactory.getInstance().getIntent(activity, CarSelect.class);
        // If exception occurs while the car is in movement it will send stop request to the smartcar
        if (healthRoverCar != null){
            carManagement.moveCar(healthRoverCar,
                Integer.parseInt(CarCommands.NO_MOVEMENT.getCarCommands()),
                Integer.parseInt(CarCommands.NO_ANGLE.getCarCommands()),
                CarCommands.CONTROL.getCarCommands(),
                activity);
        }
        // When the main page is loaded after application restart
        intent.putExtra(activity.getString(R.string.crash_error_intent), activity.getString(R.string.crash_error_message));
        activity.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(RESTART_TIME_OUT);
    }
}
