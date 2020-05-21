package se.healthrover.car_service;

import android.app.Activity;

import java.util.List;

import se.healthrover.conectivity.HealthRoverWebService;
import se.healthrover.conectivity.SqlHelper;
import se.healthrover.entities.Car;
import se.healthrover.entities.CarCommands;
import se.healthrover.entities.ObjectFactory;

public class CarManagementImp implements CarManagement {

    private HealthRoverWebService webService;
    private static List<Car> cars = ObjectFactory.getInstance().getCarList();;

    public CarManagementImp(HealthRoverWebService healthRoverWebService){
        webService = ObjectFactory.getInstance().getWebService(healthRoverWebService);
    }

    @Override
    public void checkStatus(Car healthRoverCar, Activity activity) {
        String request = healthRoverCar.getURL();// + CarCommands.STATUS.getCarCommands();
        webService.createHttpRequest(request, activity, healthRoverCar);
    }

    public void moveCar(Car healthRoverCar, int speed, int angle, Activity activity) {
        String request = healthRoverCar.getURL() + CarCommands.REQUEST.getCarCommands() + CarCommands.SPEED.getCarCommands() + speed + CarCommands.ANGLE.getCarCommands() + angle;
        webService.createHttpRequest(request, activity ,healthRoverCar);
    }


    public void addCar(Car car){
        cars.add(car);
    }

    public void removeCar(Car car){
        cars.remove(car);
    }

    public Car getCarByName(String name){
        Car carFound = null;

        for (Car car: cars) {
            if (car.getName().equals(name)){
                carFound = car;
            }
        }
        return carFound;
    }

    public Car getCarByURL(String url){
        Car carFound = null;

        for (Car car: cars) {
            if (car.getURL().equals(url)){
                carFound = car;
            }
        }
        return carFound;
    }


    public String[] getCarNames(){
        Car carFound = null;
        String[] carNames = new String[cars.size()];

        for (int i = 0; i < cars.size(); i++) {
            carNames[i] = cars.get(i).getName();
        }
        return carNames;
    }

    public List<Car> getCars() {
        return cars;
    }

    @Override
    public void updateCarName(Car car, String newName, Activity activity) {
        SqlHelper sqlHelper = ObjectFactory.getInstance().getSqlHelper(activity);
        car.setName(newName);
        sqlHelper.insertData(car);
    }


    public void loadCars(Activity activity){

        SqlHelper sqlHelper = ObjectFactory.getInstance().getSqlHelper(activity);
        getCarsOnNetwork();
        List<Car> savedCars = sqlHelper.getSavedCars();
        for (int i = 0; i < savedCars.size(); i++){
            for (int j = 0; j < cars.size(); j++){
                if (savedCars.get(i).getURL().equals(cars.get(j).getURL())){
                    cars.get(j).setName(savedCars.get(i).getName());
                }
            }
        }
    }

    private void getCarsOnNetwork() {

        cars.add(ObjectFactory.getInstance().makeCar("test1", "test"));
        cars.add(ObjectFactory.getInstance().makeCar("test2", "test1"));
        cars.add(ObjectFactory.getInstance().makeCar("test3", "test2"));
        cars.add(ObjectFactory.getInstance().makeCar("http://www.mocky.io/v2/5ec5a39e3200005900d74860", "mocky"));

    }
}