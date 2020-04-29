package se.healthrover.entities;

import org.junit.Test;

import static org.junit.Assert.*;

public class CarCommandsTest {



    @Test
    public void getCarCommandsTest() {
        String[] carCommand = new String[CarCommands.values().length];
        int i = 0;
        for (CarCommands carCommands: CarCommands.values()){
            carCommand[i] = carCommands.getCarCommands();
            i++;
        }

        boolean checkCommand = false;
        i = 0;
        for (CarCommands carCommands: CarCommands.values()){
            if (carCommands.getCarCommands().equals(carCommand[i])) {
                checkCommand = true;
            }
            else{
                checkCommand = false;
                break;
            }
            i++;
        }

        assertTrue(checkCommand);
    }
}
