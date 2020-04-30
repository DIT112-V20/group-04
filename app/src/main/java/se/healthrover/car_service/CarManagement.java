package se.healthrover.car_service;
import android.app.Activity;

import se.healthrover.entities.HealthRoverCar;

public interface CarManagement {

    boolean checkStatus(HealthRoverCar healthRoverCar);
    boolean moveCar(HealthRoverCar healthRoverCar, int speed, int angle, Activity activity);
}
