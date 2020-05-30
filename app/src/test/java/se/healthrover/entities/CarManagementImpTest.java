package se.healthrover.entities;


import com.github.javafaker.Faker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import se.healthrover.car_service.CarManagement;
import se.healthrover.car_service.CarManagementImp;
import se.healthrover.conectivity.HealthRoverWebService;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CarManagementImpTest {


    private CarManagement carManagementSpy;
    private CarManagementImp management;
    private Car healthRover;
    private List<Car> carListTestCars;



    @Before
    public void setUp(){
        management = new CarManagementImp();
        healthRover = new TestCar();
        CarManagement carManagement = new CarManagementImp();
        carManagementSpy = Mockito.spy(carManagement);
        carListTestCars = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            Car car = new Car(  "http://" + new Faker().internet().url() + "/", new Faker().name().username());
            car.setLocalDomainName(new Faker().name().name());
            carListTestCars.add(car);
            management.addCar(car);
        }
    }

    @Test
    public void getCarNamesTest(){
        String[] result = management.getCarNames();
        String[] carNames = new String[carListTestCars.size()];
        for (int i = 0; i < carNames.length; i++){
            carNames[i] = carListTestCars.get(i).getName();
        }
        assertArrayEquals(carNames, result);
    }

    @Test
    public void getCarByUrlTest(){
        Car car = carListTestCars.get(0);
        Car result = management.getCarByURL(car.getURL());
        assertEquals(car, result);

    }



    @Test
    public void checkIfRequestIsCalledTest() {
        carManagementSpy.moveCar(healthRover,0,0,"manual",null);
        Mockito.verify(carManagementSpy, Mockito.times(1)).moveCar(healthRover,0,0, "manual",null);
    }

    @Test
    public void checkIfStatusIsCalled() {
        carManagementSpy.checkStatus(healthRover,null);
        Mockito.verify(carManagementSpy, Mockito.times(1)).checkStatus(healthRover,null);
    }


    @Test
    public void getCarByNameTest(){
        Car car = carListTestCars.get(0);
        String carName = car.getName();
        Car result = management.getCarByName(carName);
        assertEquals(carName, result.getName());
        assertEquals(car, result);
    }
    @Test
    public void getCarsTest(){
        List<Car> cars = management.getCars();
        assertEquals(carListTestCars, cars);
    }

    @Test
    public void addCarTest(){
        Car car = new Car("http://" + new Faker().internet().url(), new Faker().name().username());
        car.setLocalDomainName(new Faker().name().name());
        carListTestCars.add(car);
        int startSize = management.getCars().size();
        management.addCar(car);
        assertEquals(startSize + 1 , management.getCars().size());
        assertEquals(carListTestCars, management.getCars());
    }

    @Test
    public void removeCarTest(){
        Car car = management.getCars().get(0);
        carListTestCars.remove(car);
        int startSize = management.getCars().size();
        management.removeCar(car);
        assertEquals(startSize - 1 , management.getCars().size());
        assertEquals(carListTestCars, management.getCars());
    }
    @Test
    public void setCarListTest(){
        for (int i = 1; i < management.getCars().size(); i++){
            management.getCars().remove(i);
        }
        assertNotEquals(carListTestCars, management.getCars());
        management.setCars(carListTestCars);
        assertEquals(carListTestCars, management.getCars());
    }

    @After
    public void tearDown(){
        carListTestCars = null;
        management.setCars(new ArrayList<Car>());
    }


}