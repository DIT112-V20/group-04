package se.healthrover.conectivity;

import java.util.List;

import se.healthrover.entities.Car;

public interface IpLoader {

    List<Car> loadCarsOnNetwork();
}
