package se.healthrover.entities;

public enum HealthRoverCar {
    //A list of cars used since a database is not used
    HEALTH_ROVER_CAR1("http://192.168.1.200/", "static IP"),
    HEALTH_ROVER_CAR2("http://fsdkafjl", "car2"),
    HEALTH_ROVER_CAR3("http://192.168.137.154/", "Eduroam");

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
    //Retrieves the enumerating object name by given url
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
