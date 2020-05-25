package se.healthrover.entities;

import org.junit.Test;

import se.healthrover.car_service.CarManagement;
import se.healthrover.conectivity.HealthRoverWebService;

import static junit.framework.TestCase.assertNotNull;

public class ObjectFactoryTest {

    @Test
    public void getCarManagement() {
        CarManagement carManagement = ObjectFactory.getInstance().getCarManagement();
        assertNotNull(carManagement);
    }

    @Test
    public void getWebService() {
        HealthRoverWebService healthRoverWebService = ObjectFactory.getInstance().getWebService();
        assertNotNull(healthRoverWebService);
    }

    @Test
    public void getInterfaceUtilities() {
    }

    @Test
    public void getRequestTask() {
    }

    @Test
    public void getOkHttpClient() {
    }

    @Test
    public void getIntent() {
    }

    @Test
    public void testGetIntent() {
    }

    @Test
    public void getPopupWindow() {
    }

    @Test
    public void getResponseHandler() {
    }

    @Test
    public void makeCar() {
        Car car = ObjectFactory.getInstance().makeCar("local", "test");
        assertNotNull(car);
    }

    @Test
    public void getCarList() {
    }

    @Test
    public void getSqlHelper() {
    }

    @Test
    public void getCarAdapter() {
    }

    @Test
    public void getContentValuesSQL() {
    }
}