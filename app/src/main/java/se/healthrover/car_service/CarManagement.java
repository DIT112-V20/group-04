package se.healthrover.car_service;
import se.healthrover.entities.HealthRoverCar;

public interface CarManagement {

    boolean checkStatus(HealthRoverCar healthRoverCar);
    boolean moveCar(HealthRoverCar healthRoverCar, int speed, int angle);
}
