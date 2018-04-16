package com.afolayan.med_manager;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Oluwaseyi AFOLAYAN on 4/16/2018.
 */

@RunWith(AndroidJUnit4.class)
public class MedicationActivityEspressoTest {
    @Rule
    public ActivityTestRule<MedicationActivity> mActivityRule =
            new ActivityTestRule<>(MedicationActivity.class);

    @Test
    public void ensureSearchViewWork(){
        onView(withId(R.id.search_view)).perform(typeText("hello"), closeSoftKeyboard());
        onView(withId(R.id.search_view)).check(matches(withText("hello")));
    }

    @Test
    public void checkIfFabWorks(){
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.et_medication_name)).check(matches(withContentDescription(R.string.medication_name_input)));
        onView(withId(R.id.et_medication_description)).check(matches(withContentDescription(R.string.medication_description_input)));
    }
}
