package com.jesussoto.android.bakingtime;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.jesussoto.android.bakingtime.ui.main.MainActivity;
import com.jesussoto.android.bakingtime.ui.recipedetail.RecipeDetailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> activityActivityTestRule =
            new IntentsTestRule<>(MainActivity.class);

    @Test
    public void testOpenRecipeDetail() {
        IdlingRegistry.getInstance().register(activityActivityTestRule
                .getActivity().getIdlingResource());

        onView(withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(hasComponent(RecipeDetailActivity.class.getName()));
        intended(hasExtra("extra_recipe_id", 1L));
    }

    @Test
    public void testStepsSelection() {
        IdlingRegistry.getInstance().register(activityActivityTestRule
                .getActivity().getIdlingResource());

        onView(withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withChild(withText("Step 0"))).perform(click());

        onView(allOf(withId(R.id.step_short_description), isCompletelyDisplayed()))
                .check(ViewAssertions.matches(withText("Recipe Introduction")));
    }

    @Test
    public void testStepSwipes() throws InterruptedException {
        IdlingRegistry.getInstance().register(activityActivityTestRule
                .getActivity().getIdlingResource());

        onView(withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withChild(withText("Step 0"))).perform(click());

        onView(allOf(withId(R.id.step_short_description), isCompletelyDisplayed()))
                .check(ViewAssertions.matches(withText("Recipe Introduction")));

        onView(withId(R.id.steps_pager)).perform(swipeLeft());

        Thread.sleep(1000);

        onView(allOf(withId(R.id.step_short_description), isCompletelyDisplayed()))
                .check(ViewAssertions.matches(withText("Starting prep")));

        onView(withId(R.id.steps_pager)).perform(swipeRight());

        Thread.sleep(1000);

        onView(allOf(withId(R.id.step_short_description), isCompletelyDisplayed()))
                .check(ViewAssertions.matches(withText("Recipe Introduction")));
    }
}
