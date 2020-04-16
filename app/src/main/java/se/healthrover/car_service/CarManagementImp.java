package se.healthrover.car_service;

import android.app.Activity;

import se.healthrover.conectivity.HttpService;
import se.healthrover.entities.CarCommands;
import se.healthrover.entities.HealthRoverCar;

public class CarManagementImp implements CarManagement {

    private HttpService service = new HttpService();

    public boolean moveForward(HealthRoverCar healthRoverCar) {
        return service.sendGetRequest(healthRoverCar.getUrl()+ CarCommands.FORWARD.getCarCommands());
    }

    public boolean stopCar(HealthRoverCar healthRoverCar) {
        return service.sendGetRequest(healthRoverCar.getUrl()+ CarCommands.STOP.getCarCommands());
    }

    @Override
    public boolean moveLeft(HealthRoverCar healthRoverCar) {
        return service.sendGetRequest(healthRoverCar.getUrl() + CarCommands.LEFT.getCarCommands());
    }

    @Override
    public boolean moveRight(HealthRoverCar healthRoverCar) {
        return service.sendGetRequest(healthRoverCar.getUrl() + CarCommands.RIGHT.getCarCommands());
    }

    @Override
    public boolean moveBackwards(HealthRoverCar healthRoverCar) {
        return service.sendGetRequest(healthRoverCar.getUrl() + CarCommands.BACK.getCarCommands());
    }

    public boolean checkCarOnlineStatus(HealthRoverCar healthRoverCar) {
        String request = healthRoverCar.getUrl() + CarCommands.STATUS.getCarCommands();
        return service.sendGetRequest(request);
    }


}
