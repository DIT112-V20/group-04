package se.healthrover.entities;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import se.healthrover.car_service.CarManagement;
import se.healthrover.car_service.CarManagementImp;

public class CarManagementImpTest {


    private CarManagement carManagement;
    private CarManagement carManagementSpy;

    @Before
    public void setUp(){
        carManagement = new CarManagementImp();
        carManagementSpy = Mockito.spy(carManagement);
    }


    @Test
    public void checkIfRequestIsSendTest() {
        carManagementSpy.moveCar(HealthRoverCar.HEALTH_ROVER_CAR1,0,0,null);
        Mockito.verify(carManagementSpy, Mockito.times(1)).moveCar(HealthRoverCar.HEALTH_ROVER_CAR1,0,0,null);
    }


}
