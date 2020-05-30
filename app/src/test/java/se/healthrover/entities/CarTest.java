package se.healthrover.entities;

import com.github.javafaker.Faker;

import org.junit.Assert;
import org.junit.Test;

public class CarTest {


    @Test
    public void setUrlTest(){
        Car car = new TestCar();
        String IP = car.getURL();
        String newIP =  new Faker().internet().ipV6Address();
        car.setURL(newIP);
        newIP =  "http://" + newIP + "/";
        Assert.assertEquals(newIP, car.getURL());
    }

    @Test
    public void setNameTest(){

        Car car = new TestCar();
        String name = car.getName();
        String newName =  new Faker().name().name();
        car.setName(newName);
        Assert.assertEquals(newName, car.getName());
    }

}
