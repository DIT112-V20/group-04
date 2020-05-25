package se.healthrover.test_services;


import com.github.javafaker.Faker;

import se.healthrover.entities.Car;

public class TestCar extends Car {

    public TestCar() {
        super("http://" + new Faker().internet().url(), new Faker().name().username());
    }

}

