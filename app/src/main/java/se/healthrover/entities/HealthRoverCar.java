package se.healthrover.entities;

public enum HealthRoverCar {
    //A list of cars used since a database is not used
    HEALTH_ROVER_CAR1("http://192.168.1.200/", "SmartCar"),
    HEALTH_ROVER_CAR2("http://www.mocky.io/v2/5eb7abae310000003cc8a214", "Http"),
    HEALTH_ROVER_CAR3("http://www.mocky.io/v2/5eb0765b3300005000c68e87/", "Mocky");

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

    //Retrieves the enumerating object name by given name
    public static String getCarObjectNameByCarName(String carName){
        String enumName = "";
        for (HealthRoverCar healthRoverCar: HealthRoverCar.values()){
            if (healthRoverCar.getCarName().equals(carName)) {
                enumName = healthRoverCar.name();
                break;
            }
        }
        return enumName;
    }

    public static String getCarNameByUrl(String url){
        String carName = "";
        for (HealthRoverCar healthRoverCar: HealthRoverCar.values()){
            if (healthRoverCar.getUrl().contains(url)) {
                carName = healthRoverCar.getCarName();
                break;
            }
        }
        return carName;
    }

    //Returns a list of all the names in the enum class
    public static String[] getListOfCarNames(){
        String[] carNames = new String[HealthRoverCar.values().length];
        int i = 0;
        for (HealthRoverCar healthRoverCar: HealthRoverCar.values()){
            carNames[i] = healthRoverCar.getCarName();
            i++;
        }
        return carNames;
    }

}
