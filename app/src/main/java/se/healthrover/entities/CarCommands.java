package se.healthrover.entities;

public enum CarCommands {

    //A list of predefined commands used to manage the Smartcar.
    ANGLE("&angle="), STATUS("status"), SPEED("&speed="), REQUEST("request?type=move"),

    //A list of predefined constant values for the SmartCar.
    NO_ANGLE("0"), NO_MOVEMENT("0"), VC_MAX_VELOCITY("50"), VC_MIN_VELOCITY("10"), DEFAULT_ANGLE("90");

    private final String carCommands;

    CarCommands(String carCommands) {
        this.carCommands = carCommands;
    }

    public String getCarCommands() {
        return carCommands;
    }

}