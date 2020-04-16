package se.healthrover.ui_activity_controler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import se.healthrover.R;
import se.healthrover.car_service.CarManagement;
import se.healthrover.car_service.CarManagementImp;
import se.healthrover.entities.HealthRoverCar;


public class ManualControl extends AppCompatActivity {

    private Button forwardButton;
    private Button stopButton;
    private TextView header;
    private CarManagement carManagement = new CarManagementImp();
    private String carName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_control);
        forwardButton = findViewById(R.id.forwardButton);
        stopButton = findViewById(R.id.stopButton);
        header = findViewById(R.id.header);
        //Retrieve the name of the car selected from the previous activity
        carName = getIntent().getStringExtra("carName");
        header.setText(carName);

        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               carManagement.moveForward(HealthRoverCar.valueOf(HealthRoverCar.getCarObjectName(carName)));
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                header.setText(carName);
                carManagement.stopCar(HealthRoverCar.valueOf(HealthRoverCar.getCarObjectName(carName)));
            }
        });


    }





}
