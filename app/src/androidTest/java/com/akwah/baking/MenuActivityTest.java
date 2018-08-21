package com.akwah.baking;

import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MenuActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);
    @Test
    public void clickNutellaPie_OpenDetails() {
        mainTest(0, "Nutella Pie");
    }

    @Test
    public void clickBrownies_OpenDetails() {
        mainTest(1, "Brownies");
    }

    @Test
    public void clickYellowCake_OpenDetails() {
        mainTest(2, "Yellow Cake");
    }

    @Test
    public void clickCheesecake_OpenDetails() {
        mainTest(3, "Cheesecake");
    }


    private void mainTest(int position, String title) {
        Espresso.onView(ViewMatchers.withId(R.id.main_recyclerview)).perform(RecyclerViewActions.<MainAdapter.RecipeViewHolder>actionOnItemAtPosition(position, ViewActions.click()));
        Espresso.onView(ViewMatchers.withText(title)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
