package se.healthrover.car_service;

import android.app.Activity;

import se.healthrover.conectivity.HealthRoverWebService;
import se.healthrover.conectivity.HttpService;
import se.healthrover.conectivity.HealthRoverWebSocket;
import se.healthrover.entities.CarCommands;
import se.healthrover.entities.HealthRoverCar;

public class CarManagementImp implements CarManagement {

    private HttpService service = new HttpService();

    @Override
    public boolean checkStatus(HealthRoverCar healthRoverCar, Activity activity) {
        String request = healthRoverCar.getUrl() + CarCommands.STATUS.getCarCommands();

        return   service.sendGetRequest(request);
    }

    public void moveCar(HealthRoverCar healthRoverCar, int speed, int angle, Activity activity) {
        String request = healthRoverCar.getUrl() + CarCommands.REQUEST.getCarCommands() + CarCommands.SPEED.getCarCommands() + speed + CarCommands.ANGLE.getCarCommands() + angle;
        HealthRoverWebService webService = new HealthRoverWebSocket(activity);
        webService.createWebSocket(request, activity );
    }
}