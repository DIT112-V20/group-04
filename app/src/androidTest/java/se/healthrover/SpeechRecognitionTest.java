package se.healthrover;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import se.healthrover.test_services.TestCar;
import se.healthrover.test_services.ToastMatcher;
import se.healthrover.ui_activity_controller.ManualControl;
import se.healthrover.ui_activity_controller.voice_control.SpeechRecognition;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class SpeechRecognitionTest {


    private SpeechRecognition speechRecognition;
    private static TestCar testHealthRover;
    private ToastMatcher toastMatcher;

    // Activity with pre-defined intent with car name, since the activity is launched by ManualControl and it's given a car name with launching
    @Rule
    public ActivityTestRule<SpeechRecognition> speechRecognitionActivityTestRule = new ActivityTestRule<SpeechRecognition>(SpeechRecognition.class){
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = getInstrumentation().getTargetContext();
            Intent result = new Intent(targetContext, SpeechRecognition.class);
            result.putExtra(targetContext.getString(R.string.car_name), testHealthRover);
            return result;
        }
    };

    // Launch the activity under test
    @BeforeClass
    public static void setTestHealthRover() {
        testHealthRover = new TestCar();
    }

    @Before
    public void setUp(){
        speechRecognition = speechRecognitionActivityTestRule.getActivity();
        toastMatcher = new ToastMatcher();
        Intents.init();
    }

    // Verify the context of the app under test
    @Test
    public void useAppContext(){
        Context appContext = getInstrumentation().getTargetContext();
        assertEquals("se.healthrover", appContext.getPackageName());
    }

    /* Test Case 1
       Verify that all the elements are loaded by using their IDs
       and assert that the corresponding car name is set into the header
    */

    @Test
    public void verifyIfElementsAreLoadedTest(){

        TextView textView = speechRecognition.findViewById(R.id.headerVoiceControl);
        assertNotNull(textView);
        assertEquals(testHealthRover.getName(), textView.getText());
        View view = speechRecognition.findViewById(R.id.manualControl);
        assertNotNull(view);
        view = speechRecognition.findViewById(R.id.guideButton);
        assertNotNull(view);
        view = speechRecognition.findViewById(R.id.speechButton);
        assertNotNull(view);
    }

    /* Test Case 2
       Locate the manual control button by ID,
       click on it and verify if the activity
       is loaded, along with the correct car
       name that has been passed to the activity.
    */
    @Test
    public void switchToManualControlTest(){
        onView(withId(R.id.manualControl)).perform(click());
        intended(hasComponent(ManualControl.class.getName()));
        onView(withId(R.id.manualControlHeaderText)).check(matches(withText(testHealthRover.getName())));
    }

    /* Test Case 3
       Check that the activity is loaded,
       click on the back button and
       verify that the correct activity
       is loaded (Manual Control).
    */
    @Test
    public void backButtonBackToManualControlTest(){
        Assert.assertNotNull(speechRecognition);
        Espresso.pressBack();
        intended(hasComponent(ManualControl.class.getName()));
    }

    @After
    public void tearDown(){
        speechRecognition = null;
        toastMatcher = null;
        Intents.release();
    }

}
