package se.healthrover.car_service;

import android.app.Activity;
import android.net.nsd.NsdManager;
import android.util.Log;

import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

import se.healthrover.R;
import se.healthrover.conectivity.HealthRoverWebService;
import se.healthrover.conectivity.LocalNetworkDeviceNameResolver;
import se.healthrover.conectivity.SqlHelper;
import se.healthrover.entities.Car;
import se.healthrover.entities.CarCommands;
import se.healthrover.entities.ObjectFactory;

public class CarManagementImp implements CarManagement {

    private static final String SERVICE_TYPE = "_http._tcp.";
    private static final int WEB_SERVICE_PORT = 80;
    private static final int RESOLVER_TIME_OUT = 10;
    private HealthRoverWebService webService;
    private static List<Car> cars = ObjectFactory.getInstance().createList();
    private String TAG = "smartcar";
    private NsdManager mNsdManager;
    private LocalNetworkDeviceNameResolver mDeviceNameResolver;

    public CarManagementImp(){

        webService = ObjectFactory.getInstance().getWebService();
    }

    @Override
    public void checkStatus(Car healthRoverCar, Activity activity) {
        String request = healthRoverCar.getURL() + CarCommands.STATUS.getCarCommands();
        webService.createHttpRequest(request, activity, healthRoverCar);
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

    public void moveCar(Car healthRoverCar, int speed, int angle, String controlType, Activity activity) {
        String request = healthRoverCar.getURL() + CarCommands.REQUEST.getCarCommands() + CarCommands.SPEED.getCarCommands() + speed + CarCommands.ANGLE.getCarCommands() + angle + CarCommands.CONTROL.getCarCommands() + controlType;
        webService.createHttpRequest(request, activity, healthRoverCar);
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

    public void setCars(List<Car> newCarList){
        cars = newCarList;
    }

    @Override
    public void updateCarName(Car car, String newName, Activity activity) {
        SqlHelper sqlHelper = ObjectFactory.getInstance().getSqlHelper(activity);
        car.setName(newName);
        sqlHelper.updateName(car);
    }

    // The method loads the cars from the database and the network and checks if they are any previously
    // saved cars if they are the list is updated to show the saved name
    public void loadCarsIntoList(Activity activity){

        SqlHelper sqlHelper = ObjectFactory.getInstance().getSqlHelper(activity);
        List<Car> savedCars = sqlHelper.getSavedCars();
        if (savedCars != null){
            for (int i = 0; i < savedCars.size(); i++){
                getCarsOnNetwork(activity, savedCars.get(i));
            }
        }
        else {
            sqlHelper.deleteTableContent();
            sqlHelper.insertIntoDataBase();
            savedCars = sqlHelper.getSavedCars();
            for (int i = 0; i < savedCars.size(); i++){
                getCarsOnNetwork(activity, savedCars.get(i));
            }
        }
    }

    private void getCarsOnNetwork(final Activity activity, final Car car) {

        // Synchronous device name resolution
        final LocalNetworkDeviceNameResolver nameResolver =
                new LocalNetworkDeviceNameResolver(activity.getApplicationContext(),
                        car.getLocalDomainName(), SERVICE_TYPE, WEB_SERVICE_PORT);
        Thread t = new Thread() {
            public void run() {
                try {
                    InetAddress address = nameResolver.getAddress(RESOLVER_TIME_OUT, TimeUnit.SECONDS);
                    Log.i(TAG, activity.getString(R.string.resolver_message) + address.getHostName());
                    car.setURL( address.getHostName());
                    cars.add(car);


                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        };
        t.start();

    }
}