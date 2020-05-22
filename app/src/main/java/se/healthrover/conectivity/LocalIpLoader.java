package se.healthrover.conectivity;

import java.util.ArrayList;
import java.util.List;

import se.healthrover.entities.Car;
import se.healthrover.entities.ObjectFactory;

public class LocalIpLoader implements IpLoader {

    private List<Car> cars;

    public LocalIpLoader(HealthRoverWebService healthRoverWebService)
    {
        this.cars = new ArrayList<>();
    }
    @Override
    public List<Car> loadCarsOnNetwork() {

        cars.add(ObjectFactory.getInstance().makeCar("test1", "test"));
        cars.add(ObjectFactory.getInstance().makeCar("test2", "test1"));
        cars.add(ObjectFactory.getInstance().makeCar("test3", "test2"));
        cars.add(ObjectFactory.getInstance().makeCar("http://www.mocky.io/v2/5ec5a39e3200005900d74860", "mocky"));

        return cars;
    }

}
