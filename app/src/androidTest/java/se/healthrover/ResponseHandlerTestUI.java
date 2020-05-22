package se.healthrover;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import se.healthrover.conectivity.ResponseHandler;
import se.healthrover.test_services.TestCar;
import se.healthrover.test_services.ToastMatcher;
import se.healthrover.ui_activity_controller.CarSelect;
import se.healthrover.ui_activity_controller.ManualControl;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class ResponseHandlerTestUI {

    private static TestCar testHealthRover;
    private ToastMatcher toastMatcher;
    @Rule
    public ActivityTestRule<CarSelect> carSelectActivityTestRule = new ActivityTestRule<>(CarSelect.class);
    private CarSelect carSelect;
    private ResponseHandler responseHandler;
    private static final String HTTP_STATUS_RESPONSE = "status";
    private static final String HTTP_OBSTACLE_RESPONSE = "obstacle";


    @BeforeClass
    public static void setCarSelect(){
        testHealthRover = new TestCar(TestCar.TestCarData.NAME.getTestData(), TestCar.TestCarData.ADDRESS.getTestData());
    }


    @Before
    public void setUp() {
        responseHandler = Mockito.spy(new ResponseHandler());
        carSelect = carSelectActivityTestRule.getActivity();
        toastMatcher = new ToastMatcher();
        Intents.init();
    }

    //Verify handle failure is called and verify toast
    @Test
    public void handleFailureTest()  {
        responseHandler.handleFailure(carSelect,testHealthRover);
        verify(responseHandler, times(1)).handleFailure(carSelect,testHealthRover);
        onView(withText(carSelect.getString(R.string.connection_failure) + testHealthRover.getName())).inRoot(toastMatcher).check(matches(isDisplayed()));

    }

    //Verify handle success is called, activity is loaded
    @Test
    public void handleSuccessStatusTest()  {
        responseHandler.handleSuccess(HTTP_STATUS_RESPONSE, carSelect,testHealthRover);
        verify(responseHandler, times(1)).handleSuccess(HTTP_STATUS_RESPONSE,carSelect,testHealthRover);
        intended(hasComponent(ManualControl.class.getName()));
    }


    //Verify handle success obstacle is called and check toast message
    @Test
    public void handleSuccessObstacleTest()  {
        responseHandler.handleSuccess(HTTP_OBSTACLE_RESPONSE, carSelect,testHealthRover);
        verify(responseHandler, times(1)).handleSuccess(HTTP_OBSTACLE_RESPONSE,carSelect,testHealthRover);
        onView(withText(carSelect.getString(R.string.obstacle_detection))).inRoot(toastMatcher).check(matches(isDisplayed()));

    }




    @After
    public void tearDown() {
        responseHandler = null;
        carSelect = null;
        toastMatcher = null;
        Intents.release();
    }

}
