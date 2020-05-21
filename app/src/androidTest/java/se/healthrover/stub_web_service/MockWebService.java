package se.healthrover.stub_web_service;

import android.app.Activity;
import android.content.Intent;

import se.healthrover.R;
import se.healthrover.conectivity.HealthRoverWebService;
import se.healthrover.entities.HealthRoverCar;
import se.healthrover.entities.ObjectFactory;
import se.healthrover.ui_activity_controller.ManualControl;

public class MockWebService implements HealthRoverWebService {

    @Override
    public void createHttpRequest(final String request, final Activity activity) {
        if (request.contains("status")) {

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = ObjectFactory.getInstance().getIntent(activity, ManualControl.class);
                    intent.putExtra(activity.getString(R.string.car_name), HealthRoverCar.HEALTH_ROVER_CAR1.getCarName());
                    activity.startActivity(intent);
                }
            });
        }
    }

}
