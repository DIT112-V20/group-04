package se.healthrover.car_service;

import android.app.Activity;

import se.healthrover.conectivity.HttpService;
import se.healthrover.conectivity.MyWebSocket;
import se.healthrover.entities.CarCommands;
import se.healthrover.entities.HealthRoverCar;

public class CarManagementImp implements CarManagement {

    private HttpService service = new HttpService();
    public static int countMoveRequest = 0;

    @Override
    public boolean checkStatus(HealthRoverCar healthRoverCar) {
        return service.sendGetRequest(healthRoverCar.getUrl() + CarCommands.STATUS.getCarCommands(), true);
    }

    public boolean moveCar(HealthRoverCar healthRoverCar, int speed, int angle, Activity activity) {
        String request = healthRoverCar.getUrl() + CarCommands.REQUEST.getCarCommands() + CarCommands.SPEED.getCarCommands() + speed + CarCommands.ANGLE.getCarCommands() + angle;
        MyWebSocket webSocket = new MyWebSocket(activity);
        webSocket.createWebSocket(request, activity);
        countMoveRequest++;
        return true;
    }
}