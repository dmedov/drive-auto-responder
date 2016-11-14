package com.dmedov.driveassignment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.dmedov.driveassignment.ui.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ChangeTextBehaviorTest {

    private String testStringToBeTyped;

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void initValidString() {
        testStringToBeTyped = "Simple sms response message";
    }

    @After
    public void after() {
    }

    @Test
    public void changeText_sameActivity() {
        onView(withId(R.id.response_text))
                .perform(typeText(testStringToBeTyped), closeSoftKeyboard());

        onView(withId(R.id.response_text))
                .check(matches(withText(testStringToBeTyped)));
    }


}
