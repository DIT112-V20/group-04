package se.healthrover;

public enum HealthRoverCar {

    HEALTH_ROVER_CAR1("http://192.168.1.200/", "CAR1");

    private final String url;
    private final String carName;

    HealthRoverCar(String url, String carName) {

        this.url = url;
        this.carName = carName;

    }

    public String getUrl() {
        return url;
    }

    public String getCarName() {
        return carName;
    }
}
