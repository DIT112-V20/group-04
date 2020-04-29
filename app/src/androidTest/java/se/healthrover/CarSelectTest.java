package se.healthrover;

import android.content.Context;
import android.view.View;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import se.healthrover.ui_activity_controller.CarSelect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CarSelectTest {

    @Rule
    public ActivityTestRule<CarSelect> carSelectActivityTestRule = new ActivityTestRule<CarSelect>(CarSelect.class);
    private CarSelect carSelect = null;

    @Before
    public void setUp() {
        carSelect = carSelectActivityTestRule.getActivity();
    }
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("se.healthrover", appContext.getPackageName());
    }

    @Test
    public void carSelectLaunchTest(){

        View view = carSelect.findViewById(R.id.smartCarList);

        assertNotNull(view);
    }

    @After
    public void teatDown(){
        carSelect = null;
    }
}
