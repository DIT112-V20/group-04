package se.healthrover.car_service;
import android.app.Activity;

import java.util.List;

import se.healthrover.entities.Car;
import se.healthrover.entities.ObjectFactory;
import se.healthrover.ui_activity_controller.CarSelect;

public interface CarManagement {

    void checkStatus(Car healthRoverCar, Activity activity);
    void moveCar(Car healthRoverCar, int speed, int angle, String controlType, Activity activity);

    void checkStatus(Car healthRoverCar, Activity activity);

    void moveCar(Car healthRoverCar, int speed, int angle, Activity activity);

    void loadCars(Activity activity);

    Car getCarByName(String carName);
    List<Car> getCars();

    void updateCarName(Car car, String newName, Activity activity);
}
