package se.healthrover.entities;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import se.healthrover.car_service.CarManagement;
import se.healthrover.car_service.CarManagementImp;
import se.healthrover.conectivity.HealthRoverWebService;

public class CarManagementImpTest {


    private CarManagement carManagementSpy;
    private HealthRoverWebService healthRoverCar;
    private Car healthRover;

    @Before
    public void setUp(){
        healthRover = new TestCar();
        CarManagement carManagement = new CarManagementImp();
        carManagementSpy = Mockito.spy(carManagement);
    }



    @Test
    public void checkIfRequestIsSendTest() {
        carManagementSpy.moveCar(healthRover,0,0,"manual",null);
        Mockito.verify(carManagementSpy, Mockito.times(1)).moveCar(healthRover,0,0, "manual",null);
    }


}
