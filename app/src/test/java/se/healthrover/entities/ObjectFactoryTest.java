package se.healthrover.entities;

import android.content.ContentValues;

import com.github.javafaker.Faker;

import org.junit.Test;

import java.util.List;

import okhttp3.OkHttpClient;
import se.healthrover.car_service.CarManagement;
import se.healthrover.conectivity.HealthRoverWebService;
import se.healthrover.conectivity.ResponseHandler;
import se.healthrover.ui_activity_controller.utilities.UserInterfaceUtilities;

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
        UserInterfaceUtilities userInterfaceUtilities = ObjectFactory.getInstance().getInterfaceUtilities();
        assertNotNull(userInterfaceUtilities);
    }


    @Test
    public void getOkHttpClient() {
        OkHttpClient okHttpClient = ObjectFactory.getInstance().getOkHttpClient();
        assertNotNull(okHttpClient);
    }



    @Test
    public void getResponseHandler() {
        ResponseHandler responseHandler = ObjectFactory.getInstance().getResponseHandler();
        assertNotNull(responseHandler);
    }

    @Test
    public void makeCar() {
        Car car = ObjectFactory.getInstance().makeCar("local", "test");
        car.setLocalDomainName(new Faker().name().name());
        assertNotNull(car);
    }

    @Test
    public void getCarList() {
        List<Car> cars = ObjectFactory.getInstance().createList();
        assertNotNull(cars);
    }


    @Test
    public void getContentValuesSQL() {
       ContentValues contentValues = ObjectFactory.getInstance().getContentValuesSQL();
       assertNotNull(contentValues);
    }
}