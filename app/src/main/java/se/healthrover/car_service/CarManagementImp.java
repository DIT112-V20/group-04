package se.healthrover.car_service;

import android.app.Activity;

import se.healthrover.conectivity.HealthRoverWebService;
import se.healthrover.conectivity.OkHttpWebService;
import se.healthrover.entities.CarCommands;
import se.healthrover.entities.HealthRoverCar;

public class CarManagementImp implements CarManagement {

    private HealthRoverWebService webService;

    public CarManagementImp(){
        webService = new OkHttpWebService();
    }

    @Override
    public void checkStatus(HealthRoverCar healthRoverCar, Activity activity) {
        String request = healthRoverCar.getUrl() + CarCommands.STATUS;
        webService.createHttpRequest(request, activity);
    }

    public void moveCar(HealthRoverCar healthRoverCar, int speed, int angle, Activity activity) {
        String request = healthRoverCar.getUrl() + CarCommands.REQUEST.getCarCommands() + CarCommands.SPEED.getCarCommands() + speed + CarCommands.ANGLE.getCarCommands() + angle;
        webService.createHttpRequest(request, activity );
    }
}