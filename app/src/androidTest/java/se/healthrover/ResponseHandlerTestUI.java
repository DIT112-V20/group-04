package se.healthrover;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import se.healthrover.conectivity.ResponseHandler;
import se.healthrover.entities.HealthRoverCar;
import se.healthrover.ui_activity_controller.CarSelect;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ResponseHandlerTestUI {

    private static HealthRoverCar testHealthRover;
    @Rule
    public ActivityTestRule<CarSelect> carSelectActivityTestRule = new ActivityTestRule<CarSelect>(CarSelect.class);
    private CarSelect carSelect;
    private ResponseHandler responseHandler;
    private static final String HTTP_STATUS_RESPONSE = "status";
    private static final String HTTP_OBSTACLE_RESPONSE = "obstacle";


    @BeforeClass
    public static void setCarSelect(){
        testHealthRover = HealthRoverCar.HEALTH_ROVER_CAR1;
    }


    @Before
    public void setUp() {
        responseHandler = Mockito.spy(new ResponseHandler());
        carSelect = carSelectActivityTestRule.getActivity();
    }

    @Test
    public void handleFailureTest()  {
        responseHandler.handleFailure(carSelect,testHealthRover.getUrl());
        verify(responseHandler, times(1)).handleFailure(carSelect,testHealthRover.getUrl());

    }

    @Test
    public void handleSuccessStatusTest()  {
        responseHandler.handleSuccess(HTTP_STATUS_RESPONSE, carSelect,testHealthRover.getUrl() + HTTP_STATUS_RESPONSE);
        verify(responseHandler, times(1)).handleSuccess(HTTP_STATUS_RESPONSE,carSelect,testHealthRover.getUrl()+HTTP_STATUS_RESPONSE);

    }

    @Test
    public void handleSuccessObstacleTest()  {
        responseHandler.handleSuccess(HTTP_OBSTACLE_RESPONSE, carSelect,testHealthRover.getUrl() + HTTP_STATUS_RESPONSE);
        verify(responseHandler, times(1)).handleSuccess(HTTP_OBSTACLE_RESPONSE,carSelect,testHealthRover.getUrl()+HTTP_STATUS_RESPONSE);

    }


    @After
    public void tearDown()  {
        responseHandler = null;
        carSelect = null;
    }



}
