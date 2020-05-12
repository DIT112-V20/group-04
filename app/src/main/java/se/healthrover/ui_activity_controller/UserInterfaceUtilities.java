package se.healthrover.ui_activity_controller;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

//Common utilities used in the application
public class UserInterfaceUtilities {
    private static int TOAST_OFFSET = 165;

    //Shows a customized toast message with the given string
    public void showCustomToast(Context applicationContext, String text) {
        Toast customToast = Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT);
        customToast.setGravity(Gravity.TOP,0, TOAST_OFFSET);
        customToast.show();
    }
}