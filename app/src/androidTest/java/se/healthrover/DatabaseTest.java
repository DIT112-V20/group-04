package se.healthrover;

import android.app.Activity;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.github.javafaker.Faker;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import se.healthrover.conectivity.SqlHelper;
import se.healthrover.entities.Car;
import se.healthrover.entities.ObjectFactory;
import se.healthrover.test_services.TestCar;
import se.healthrover.ui_activity_controller.car_selection.CarSelect;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {


    private SqlHelper sqlHelper;
    private TestCar car;
    private List<Car> carListTestCars;
    private List<Car> carList;
    private List<Car> dataBase;
    private Activity activity;

    @Rule
    public ActivityTestRule<CarSelect> carSelectActivityTestRule = new ActivityTestRule<>(CarSelect.class);


    @Before
    public void setUp() {
        activity = carSelectActivityTestRule.getActivity();
        carListTestCars = new ArrayList<>();
        dataBase = new ArrayList<>();
        carList = new ArrayList<>();
        sqlHelper = ObjectFactory.getInstance().getSqlHelper(activity);
        dataBase = sqlHelper.getSavedCars();
        sqlHelper.deleteTableContent();
        for (int i = 0; i < 10; i++){
            Car car = new Car("http://" + new Faker().internet().url(), new Faker().name().username());
            car.setLocalDomainName(new Faker().name().name());
            carListTestCars.add(car);
        }
        for (int i = 0; i < carListTestCars.size(); i++){
            sqlHelper.insertData(carListTestCars.get(i));
            carList.add(carListTestCars.get(i));
        }
    }

    @Test
    public void retrieveItemsFromDatabaseTest(){
        List<Car> result = sqlHelper.getSavedCars();
        assertEquals(carList, result);
    }
    @Test
    public void insertIntoDatabaseTest(){
        Car car = new Car("http://" + new Faker().internet().url(), new Faker().name().username());
        car.setLocalDomainName(new Faker().name().name());
        sqlHelper.insertData(car);
        List<Car> result = sqlHelper.getSavedCars();
        carList.add(car);
        carListTestCars.add(car);
        assertEquals(carList, result);
    }

    @Test
    public void removeCar(){
        Car car = carListTestCars.get(0);
        sqlHelper.deleteCar(car);
        carList.remove(car);
        carListTestCars.remove(car);
        List<Car> result = sqlHelper.getSavedCars();
        assertEquals(carList, result);
    }

    @Test
    public void updateCarNameTest(){
        Car car = carListTestCars.get(0);
        String name = "new Name";
        car.setName(name);
        sqlHelper.updateName(car);
        List<Car> result = sqlHelper.getSavedCars();
        assertEquals(carList, result);
        Car updatedCar = sqlHelper.getCarByName(name);
        assertEquals(car,updatedCar);
    }

    @Test
    public void findSpecificCarByNameTest(){
        Car car = carListTestCars.get(0);
        Car result = sqlHelper.getCarByName(car.getName());
        assertEquals(car, result);
    }


    @After
    public void tearDown()  {
        sqlHelper.deleteTableContent();
        if (dataBase != null){
            for (int i = 0; i < dataBase.size(); i++){
                sqlHelper.insertData(dataBase.get(i));
            }
            assertEquals(dataBase, sqlHelper.getSavedCars());
        }
        carList = null;
        dataBase = null;
        carListTestCars = null;
        activity = null;
    }
}
