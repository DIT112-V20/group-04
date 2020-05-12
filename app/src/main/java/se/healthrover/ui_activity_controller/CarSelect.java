package se.healthrover.ui_activity_controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import se.healthrover.R;
import se.healthrover.car_service.CarManagement;
import se.healthrover.car_service.CarManagementImp;
import se.healthrover.entities.HealthRoverCar;

public class CarSelect extends Activity{

    private Button connectToCarSelected;
    private ListView carList;
    private HealthRoverCar healthRoverCar;
    private boolean carOnlineConnection;
    private CarManagement carManagement = new CarManagementImp();
    private static int TOAST_OFFSET = 165;

    //Create the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();

    }
    //Refresh the activity, used on back button press
    @Override
    protected void onRestart() {
        super.onRestart();
        initialize();
    }

    //Used to initialize the elements on the activity once it¨s loaded
    private void initialize(){
        //setting up by default everything to false and loading the car names into the listView and adapter
        carOnlineConnection = false;
        healthRoverCar = null;
        setContentView(R.layout.car_select);
        connectToCarSelected = findViewById(R.id.connectToCar);

        ArrayAdapter adapter = new ArrayAdapter<>(this,
                R.layout.car_select_list_item, HealthRoverCar.getListOfCarNames());

        carList = findViewById(R.id.smartCarList);
        carList.setAdapter(adapter);
        //Once a car is selected the name is retrieved and used to initialize the car object that is to be controlled
        carList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String carName = carList.getItemAtPosition(position).toString();
                healthRoverCar = HealthRoverCar.valueOf(HealthRoverCar.getCarObjectNameByCarName(carName));

                Toast selectionToast = Toast.makeText(getApplicationContext(), "You selected " + carName, Toast.LENGTH_SHORT);
                selectionToast.setGravity(Gravity.TOP, 0 , TOAST_OFFSET);
                selectionToast.show();
            }
        });


        //Connect to car button is pressed and we call the get status method in order to verify that the car is online
        connectToCarSelected.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (healthRoverCar == null){
                    Toast noCarSelectedToast = Toast.makeText(getApplicationContext(), "Select a car...", Toast.LENGTH_SHORT);
                    noCarSelectedToast.setGravity(Gravity.TOP,0, TOAST_OFFSET);
                    noCarSelectedToast.show();
                }else {
                    carManagement.checkStatus(healthRoverCar, CarSelect.this);
                }
            }
        });
    }
    //Using back button to exit application
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        finish();
    }

}
