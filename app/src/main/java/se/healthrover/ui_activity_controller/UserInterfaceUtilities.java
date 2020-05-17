package se.healthrover.ui_activity_controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import se.healthrover.entities.ObjectFactory;

//Common utilities used in the application
public class UserInterfaceUtilities {
    private static int TOAST_OFFSET = 165;

    //Shows a customized toast message with the given string
    public void showCustomToast(Context applicationContext, String text) {
        Toast customToast = Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT);
        customToast.setGravity(Gravity.TOP,0, TOAST_OFFSET);
        customToast.show();
    }

    public void showCustomPopup(Context applicationContext, int layout, View view) {
        // Inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        final View popupView = inflater.inflate(layout, null);

        // Create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = ObjectFactory.getInstance().getPopupWindow(popupView, width, height, focusable);
        popupWindow.setElevation(32);

        // Dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

        //Finally, show the popup window
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
}