package se.healthrover.conectivity;

import android.app.Activity;

import se.healthrover.entities.Car;

public interface HealthRoverWebService {

    void createHttpRequest(String request, Activity activity, Car car);
}
