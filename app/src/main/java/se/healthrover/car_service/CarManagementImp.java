package se.healthrover.car_service;

import android.app.Activity;

import se.healthrover.conectivity.HealthRoverWebService;
import se.healthrover.entities.CarCommands;
import se.healthrover.entities.HealthRoverCar;
import se.healthrover.entities.ObjectFactory;

public class CarManagementImp implements CarManagement {

    private HealthRoverWebService webService;

    public CarManagementImp(){
        webService = ObjectFactory.getInstance().getWebService();
    }

    @Override
    public void checkStatus(HealthRoverCar healthRoverCar, Activity activity) {
        String request = healthRoverCar.getUrl();// + CarCommands.STATUS.getCarCommands();
        webService.createHttpRequest(request, activity);
    }

    public void moveCar(HealthRoverCar healthRoverCar, int speed, int angle, String controlType, Activity activity) {
        String request = healthRoverCar.getUrl() + CarCommands.REQUEST.getCarCommands() + CarCommands.SPEED.getCarCommands() + speed + CarCommands.ANGLE.getCarCommands() + angle + CarCommands.CONTROL.getCarCommands() + controlType;
        webService.createHttpRequest(request, activity );
    }
}