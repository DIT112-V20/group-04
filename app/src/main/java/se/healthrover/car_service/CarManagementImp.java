package se.healthrover.car_service;

import android.app.Activity;

import se.healthrover.conectivity.HttpService;
import se.healthrover.conectivity.HealthRoverWebSocket;
import se.healthrover.entities.CarCommands;
import se.healthrover.entities.HealthRoverCar;

public class CarManagementImp implements CarManagement {

    private HttpService service = new HttpService();
    //Testing TODO remove counter
    public static int countMoveRequest = 0;

    @Override
    public boolean checkStatus(HealthRoverCar healthRoverCar) {
        return service.sendGetRequest(healthRoverCar.getUrl() + CarCommands.STATUS.getCarCommands());
    }

    public boolean moveCar(HealthRoverCar healthRoverCar, int speed, int angle, Activity activity) {
        String request = healthRoverCar.getUrl() + CarCommands.REQUEST.getCarCommands() + CarCommands.SPEED.getCarCommands() + speed + CarCommands.ANGLE.getCarCommands() + angle;
        HealthRoverWebSocket webSocket = new HealthRoverWebSocket(activity);
        webSocket.createWebSocket(request, activity);
        //Testing TODO remove counter
        countMoveRequest++;
        return true;
    }
}