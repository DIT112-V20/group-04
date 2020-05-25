package se.healthrover.entities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;

import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import se.healthrover.R;
import se.healthrover.car_service.CarManagement;
import se.healthrover.car_service.CarManagementImp;
import se.healthrover.conectivity.HealthRoverWebService;
import se.healthrover.conectivity.OkHttpWebService;
import se.healthrover.conectivity.ResponseHandler;
import se.healthrover.conectivity.SqlHelper;
import se.healthrover.ui_activity_controller.car_selection.CarAdapter;
import se.healthrover.ui_activity_controller.utilities.ActivityExceptionHandler;
import se.healthrover.ui_activity_controller.utilities.UserInterfaceUtilities;
import se.healthrover.ui_activity_controller.voice_control.RequestTask;

public class ObjectFactory {

    private static ObjectFactory instance = null;

    private ObjectFactory() {}

    public static ObjectFactory getInstance() {
        if (instance == null) {
            synchronized(ObjectFactory.class) {
                 instance = new ObjectFactory();
            }
        }
        return instance;
     }


    public CarManagement getCarManagement() {
            return new CarManagementImp();
    }

    public HealthRoverJoystick getHealthRoverJoystick(Activity activity) {
        return new HealthRoverJoystick(activity);
    }
    public HealthRoverWebService getWebService() {
            return new OkHttpWebService();
    }

    public ActivityExceptionHandler getExceptionHandler(Activity activity, Car healthRoverCar){
        return new ActivityExceptionHandler(activity, healthRoverCar);
    }

    public UserInterfaceUtilities getInterfaceUtilities(){
       return new UserInterfaceUtilities();
    }

    public RequestTask getRequestTask(Activity activity, SessionName session, SessionsClient sessionsClient, QueryInput queryInput){
       return new RequestTask(activity, session, sessionsClient , queryInput);
    }

    public OkHttpClient getOkHttpClient(){
        return new OkHttpClient();
    }

    public Intent getIntent(Activity oldActivity, Class<?> newActivity){
        return new Intent(oldActivity,newActivity);
    }
    public Intent getIntent(String recognizerIntent){
        return new Intent(recognizerIntent);
    }

    public PopupWindow getPopupWindow(View popupView, int width, int height, boolean focusable){
        return new PopupWindow(popupView, width, height, focusable);
    }

    public ResponseHandler getResponseHandler() {
        return new ResponseHandler();
    }

    public Car makeCar(String URL, String name){
        return new Car(URL, name);
    }

    public List<Car> getCarList() {
        return new ArrayList<>();
    }

    public SqlHelper getSqlHelper(Activity activity){
        return new SqlHelper(activity);
    }


    public ArrayAdapter getCarAdapter(Activity activity, int list_item, List<Car> cars) {
        return new CarAdapter(activity, R.layout.list_item,cars);
    }

    public ContentValues getContentValuesSQL() {
        return new ContentValues();
    }
}
