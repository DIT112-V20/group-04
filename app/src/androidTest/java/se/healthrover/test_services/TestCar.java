package se.healthrover.test_services;

import se.healthrover.entities.Car;

public class TestCar extends Car {

    public TestCar(String URL, String name) {
        super(URL, name);
    }



   public enum TestCarData{

        NAME("new"),
        ADDRESS("http://localhost/");

        private final String testData;

        TestCarData(String testData) {
            this.testData = testData;
        }

        public String getTestData(){
            return testData;
        }
    }
}

