package se.healthrover.car_service;

import se.healthrover.conectivity.HttpService;
import se.healthrover.entities.CarCommands;
import se.healthrover.entities.HealthRoverCar;

public class CarManagementImp implements CarManagement {

    private HttpService service = new HttpService();


    @Override
    public boolean checkStatus(HealthRoverCar healthRoverCar) {
        return service.sendGetRequest(healthRoverCar.getUrl() + CarCommands.STATUS.getCarCommands());
    }

    public boolean moveCar(HealthRoverCar healthRoverCar, int speed, int angle) {
        String request = healthRoverCar.getUrl() + CarCommands.MOVE.getCarCommands() + CarCommands.SPEED.getCarCommands() + speed + CarCommands.ANGLE.getCarCommands() + angle;
        return service.sendGetRequest(request);
    }


}
