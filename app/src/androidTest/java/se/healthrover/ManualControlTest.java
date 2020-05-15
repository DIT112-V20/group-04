package se.healthrover;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import se.healthrover.entities.HealthRoverCar;
import se.healthrover.entities.HealthRoverJoystick;
import se.healthrover.ui_activity_controller.CarSelect;
import se.healthrover.ui_activity_controller.ManualControl;
import se.healthrover.ui_activity_controller.voice_control.SpeechRecognition;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ManualControlTest {

    private ManualControl manualControl;
    private SpeechRecognition speechRecognition;
    private static HealthRoverCar testHealthRover;

    // Activity with pre-defined intent with car name, since the activity is launched by CarSelect and it's given a car name with launching
    @Rule
    public ActivityTestRule<ManualControl> manualControlActivityTestRule = new ActivityTestRule<ManualControl>(ManualControl.class) {
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = getInstrumentation().getTargetContext();
            Intent result = new Intent(targetContext, ManualControl.class);
            result.putExtra(targetContext.getString(R.string.car_name), testHealthRover.getCarName());
            return result;
        }
    };


    // Initialize test object
    @BeforeClass
    public static void setTestHealthRover(){
        testHealthRover = HealthRoverCar.HEALTH_ROVER_CAR1;
    }

    // Launch activity under test
    @Before
    public void setUp() throws Exception {
        manualControl = manualControlActivityTestRule.getActivity();
        Intents.init();
    }

    // Verify the context of the app under test
    @Test
    public void testCase_0_useAppContext() {
        Context appContext = getInstrumentation().getTargetContext();
        assertEquals("se.healthrover", appContext.getPackageName());
    }

    /* Test Case 1
     *   verify that all the elements are loaded by using IDs
     *   assert that the correct car name is set into the header*/
    @Test
    public void testCase_1_verifyIfElementsAreLoadedTest() {

        View view = manualControl.findViewById(R.id.joystick);
        assertNotNull(view);
        TextView textView = manualControl.findViewById(R.id.chooseCarText);
        assertNotNull(textView);
        assertEquals(testHealthRover.getCarName() , textView.getText());
        view = manualControl.findViewById(R.id.voiceControl);
        assertNotNull(view);
        view = manualControl.findViewById(R.id.textSpeedHeader);
        assertNotNull(view);
        view = manualControl.findViewById(R.id.textView_strength);
        assertNotNull(view);
        view = manualControl.findViewById(R.id.textAngleHeader);
        assertNotNull(view);
        view = manualControl.findViewById(R.id.textView_angle);
        assertNotNull(view);
        view = manualControl.findViewById(R.id.layoutAngle);
        assertNotNull(view);
        view = manualControl.findViewById(R.id.layoutSpeed);
        assertNotNull(view);

    }

    /* Test Case 2
    *   Locate the voice control button by ID,
    *   click on the voice control button,
    *   verify that the correct activity is loaded
    *   verify that the correct car name has been passed to the activity*/
    @Test
    public void testCase_2_switchToVoiceControlTest() {
        onView(withId(R.id.voiceControl)).perform(click());
        intended(hasComponent(SpeechRecognition.class.getName()));
        onView(withId(R.id.headerVoiceControl)).check(matches(withText(testHealthRover.getCarName())));
    }

    /* Test Case 3
     *   check that the activity is loaded,
     *   click the emulator back button,
     *   verify that the correct activity is loaded*/
    @Test
    public void testCase_3_backButtonBackToCarSelectTest() {
        assertNotNull(manualControl);
        Espresso.pressBack();
        intended(hasComponent(CarSelect.class.getName()));
    }
    /* Test Case 4
     *   Test the movement of the joystick
     *   assert that the joystick is loaded.
     *   move the joystick up,
     *   move the joystick down,
     *   move the joystick left,
     *   move the joystick right,
     *   verify that the joystick is reset to start position - start position x = 50 and y = 50*/
    @Test
    public void testCase_4_checkJoystickMovement() {
        HealthRoverJoystick joystick = manualControl.findViewById(R.id.joystick);
        assertNotNull(joystick);
        onView(withId(R.id.joystick)).perform(swipeUp());
        onView(withId(R.id.joystick)).perform(swipeDown());
        onView(withId(R.id.joystick)).perform(swipeLeft());
        onView(withId(R.id.joystick)).perform(swipeRight());
        int joystickStartPosition = 50;
        assertEquals(joystickStartPosition, joystick.getNormalizedX());
        assertEquals(joystickStartPosition, joystick.getNormalizedY());
    }

    // Close activity under test
    @After
    public void tearDown() throws Exception {
        manualControl = null;
        Intents.release();
    }


}
