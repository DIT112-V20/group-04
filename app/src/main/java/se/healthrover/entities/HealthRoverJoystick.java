package se.healthrover.entities;

import android.content.Context;
import android.util.AttributeSet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class HealthRoverJoystick extends JoystickView {


    public HealthRoverJoystick(Context context) {
        super(context);
    }

    public HealthRoverJoystick(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HealthRoverJoystick(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    //Convert strength from joystick to the corresponding car speed taking only 10ths values
    public int convertSpeed(int strength, int angle) {
        if (angle > 180) {
            return - (strength/10)*10;
        } else {
            return (strength/10)*10;
        }
    }

    //Converts angle from joystick to the corresponding car angles, taking only 10ths values
    public int convertAngle(int angle) {
        if (angle <= 90 && angle >= 0) {
            return ((90 - angle)/10)*10;
        } else if (angle > 90 && angle <= 180) {
            return (((angle - 90) * (-1))/10)*10;
        } else if (angle > 180 && angle <= 270) {
            return ((angle - 270)/10)*10;
        } else {
            return ((angle - 270)/10)*10;
        }
    }


}
