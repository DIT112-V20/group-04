package se.healthrover.ui_activity_controller;

import android.app.Activity;
import android.os.Bundle;
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
import se.healthrover.ui_activity_controller.error_handling.ActivityExceptionHandler;

public class CarSelect extends Activity{

    private Button connectToCarSelected;
    private ListView carList;
    private HealthRoverCar healthRoverCar;
    private boolean carOnlineConnection;
    private CarManagement carManagement = new CarManagementImp();

    //Create the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ActivityExceptionHandler(this, healthRoverCar));
        initialize();

    }
    //Refresh the activity, used on back button press
    @Override
    protected void onRestart() {
        super.onRestart();
        Thread.setDefaultUncaughtExceptionHandler(new ActivityExceptionHandler(this, healthRoverCar));
        initialize();
    }

    //Used to initialize the elements on the activity once it¨s loaded
    private void initialize(){
        checkForErrorMessage();
        //setting up by default everything to false and loading the car names into the listView and adapter
        carOnlineConnection = false;
        healthRoverCar = null;
        setContentView(R.layout.car_select);
        connectToCarSelected = findViewById(R.id.connectToCar);

        ArrayAdapter adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, HealthRoverCar.getListOfCarNames());

        carList = findViewById(R.id.smartCarList);
        carList.setAdapter(adapter);

        //Once a car is selected the name is retrieved and used to initialize the car object that is to be controlled
        carList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String carName = carList.getItemAtPosition(position).toString();
                healthRoverCar = HealthRoverCar.valueOf(HealthRoverCar.getCarObjectNameByCarName(carName));
                Toast.makeText(getApplicationContext(), "You selected " + carName, Toast.LENGTH_SHORT).show();
            }
        });

        //Connect to car button is pressed and we call the get status method in order to verify that the car is online
        connectToCarSelected.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (healthRoverCar == null){
                    Toast.makeText(getApplicationContext(), "Select a car...", Toast.LENGTH_SHORT).show();
                }else {
                    carManagement.checkStatus(healthRoverCar, CarSelect.this);

                }
            }
        });
    }
    //Checks if the activity is loaded after a crash and prints it out if it exists
    private void checkForErrorMessage() {
        String errorMessage = getIntent().getStringExtra(getString(R.string.crashErrorIntent));
        if (errorMessage!=null){
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    //Using back button to exit application
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        finish();
    }

}
