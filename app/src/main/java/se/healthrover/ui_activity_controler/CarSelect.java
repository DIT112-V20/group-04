package se.healthrover.ui_activity_controler;

import android.app.Activity;
import android.content.Intent;
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
        //setting up by default everything to false and loading the car names into the listview and adapter
        carOnlineConnection = false;
        healthRoverCar = null;
        setContentView(R.layout.car_select);
        connectToCarSelected = findViewById(R.id.connectToCar);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, HealthRoverCar.getListOfCarNames());

        carList = (ListView) findViewById(R.id.smartCarList);
        carList.setAdapter(adapter);

        //Once a car is selected the name is retrieved and used to initialize the car object that is to be controlled
        carList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String carName = carList.getItemAtPosition(position).toString();
                healthRoverCar = HealthRoverCar.valueOf(HealthRoverCar.getCarObjectName(carName));

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

                    carOnlineConnection = carManagement.checkCarOnlineStatus(healthRoverCar);
                    if (!carOnlineConnection) {
                        Toast.makeText(getApplicationContext(), "Car is offline...", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(CarSelect.this, ManualControl.class);
                        intent.putExtra("carName", healthRoverCar.getCarName());
                        startActivity(intent);
                    }
                }
            }
        });
    }



}
