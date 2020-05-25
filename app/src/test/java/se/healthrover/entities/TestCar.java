package se.healthrover.entities;

import com.github.javafaker.Faker;

class TestCar extends Car {


    TestCar() {
        super("http://" + new Faker().internet().url(), new Faker().name().username());
        super.setID(new Faker().idNumber().ssnValid());
    }

}

