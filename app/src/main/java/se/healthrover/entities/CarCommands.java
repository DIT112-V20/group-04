package se.healthrover.entities;

public enum CarCommands {

    //A list of predefined commands used to manage the car
    ANGLE("&angle="), STATUS("status"), SPEED("&speed="), REQUEST("request?type=move");

    private final String carCommands;

    CarCommands(String carCommands) {
        this.carCommands = carCommands;
    }

    public String getCarCommands() {
        return carCommands;
    }

}