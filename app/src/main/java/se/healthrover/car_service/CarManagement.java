package se.healthrover.car_service;
import se.healthrover.entities.HealthRoverCar;

public interface CarManagement {

    boolean moveForward(HealthRoverCar healthRoverCar);
    boolean stopCar(HealthRoverCar healthRoverCar);
    boolean moveLeft(HealthRoverCar healthRoverCar);
    boolean moveRight(HealthRoverCar healthRoverCar);
    boolean moveBackwards(HealthRoverCar healthRoverCar);
    boolean checkCarOnlineStatus(HealthRoverCar healthRoverCar);

}
