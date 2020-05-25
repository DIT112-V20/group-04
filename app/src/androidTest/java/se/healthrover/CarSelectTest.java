package se.healthrover;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import se.healthrover.entities.Car;
import se.healthrover.entities.ObjectFactory;
import se.healthrover.test_services.TestCar;
import se.healthrover.ui_activity_controller.ManualControl;
import se.healthrover.ui_activity_controller.car_selection.CarSelect;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CarSelectTest  {

    private CarSelect carSelect;
    private static Car testHealthRover;
    private ManualControl manualControl;


    @Rule
    public ActivityTestRule<CarSelect> carSelectActivityTestRule = new ActivityTestRule<>(CarSelect.class);
    @Rule
    public ActivityTestRule<ManualControl> manualControlActivityTestRule = new ActivityTestRule<>(ManualControl.class, true, false);

    @BeforeClass
    public static void setCarSelect(){
        testHealthRover = new TestCar();
    }


    @Before
    public void setUp() {
        carSelect = carSelectActivityTestRule.getActivity();
        Intents.init();
    }
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = getInstrumentation().getTargetContext();
        assertEquals("se.healthrover", appContext.getPackageName());
    }

    /* Test Case 1
     *   verify that all the elements are loaded by using IDs*/
    @Test
    public void verifyIfElementsAreLoadedTest(){
        //Buttons
        View view = carSelect.findViewById(R.id.infoButton);
        assertNotNull(view);
        view = carSelect.findViewById(R.id.connectToCarButton);
        assertNotNull(view);
        //SmartCar list
        view = carSelect.findViewById(R.id.smartCarList);
        assertNotNull(view);
        //Text field
        view = carSelect.findViewById(R.id.chooseCarText);
        assertNotNull(view);
    }
    /* Test Case 2
     *   Locate the SmartCar list
     *   Click an existing row in the SmartCar list
     *   Locate the Connect button by ID,
     *   click on the Connect button,
     *   verify that the correct activity is loaded
     *   verify that the correct car name has been passed to the activity*/
    @Test
    public void switchToManualControlTest() {
        final ListView list = carSelect.findViewById(R.id.smartCarList);
        assertNotNull(list);
        final View button = carSelect.findViewById(R.id.connectToCarButton);
        assertNotNull(button);
        final List<Car> cars = new ArrayList<>();
        cars.add(testHealthRover);
        assertNotNull(cars);
        final ArrayAdapter adapter = ObjectFactory.getInstance().getCarAdapter(carSelect,
                R.layout.list_item,cars);
        assertNotNull(adapter);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                View firstItem = list.getAdapter().getView(0, null, null);
                assertNotNull(firstItem);
            }
        };
        carSelect.runOnUiThread(runnable);
        InstrumentationRegistry.getInstrumentation().waitForIdle(runnable);
        onData(anything()).inAdapterView(withId(R.id.smartCarList)).atPosition(0).perform(click());
        onView(withId(R.id.connectToCarButton)).perform(click());
        Intent intent = ObjectFactory.getInstance().getIntent(carSelect, ManualControl.class);
        intent.putExtra(carSelect.getString(R.string.car_name), testHealthRover);
        manualControlActivityTestRule.launchActivity(intent);
        intended(hasComponent(hasClassName(ManualControl.class.getName())));
        onView(withId(R.id.manualControlHeaderText)).check(matches(isDisplayed()));
        onView(withId(R.id.manualControlHeaderText)).check(matches(withText(testHealthRover.getName())));
    }
    /* Test Case 3
     *   check that the activity is loaded,
     *   click the info button,
     *   verify that the info popup is loaded,
     *   verify the text in the popup.
     */
    @Test
    public void infoButtonDisplaysInfoPopup(){
        assertNotNull(carSelect);
        onView(withId(R.id.infoButton)).perform(click());
        onView(withId(R.id.infoPopup)).check(matches(isDisplayed()));
        onView(withText(R.string.info_text)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown(){
        carSelect = null;
        Intents.release();
    }
}