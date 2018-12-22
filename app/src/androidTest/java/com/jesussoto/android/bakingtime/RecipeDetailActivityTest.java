package com.jesussoto.android.bakingtime;

import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.jesussoto.android.bakingtime.api.WebService;
import com.jesussoto.android.bakingtime.ui.main.MainActivity;
import com.jesussoto.android.bakingtime.ui.recipedetail.RecipeDetailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest {

    @Rule
    public IntentsTestRule<RecipeDetailActivity> activityActivityTestRule =
            new IntentsTestRule<>(RecipeDetailActivity.class);

    @Mock
    WebService mMockWebService;

    /*@Before
    void setup() {
        MockitoAnnotations.initMocks(this);
        BakingTimeApp app = (BakingTimeApp) InstrumentationRegistry.getTargetContext()
                .getApplicationContext();

        TestAppComponent testAppComponent = DaggerTestAppComponent.builder()
                .appModule(new TestAppModule(mMockWebService))
                .databaseModule(new DatabaseModule())
                .build();

        app.setAppComponent(testAppComponent);
    }*/

    public void testStepsSelection() {
        Intent intent = new Intent();
        //intent.putExtra("extra_recipe_id")
        //activityActivityTestRule.launchActivity(1);

        onView(withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(hasComponent(RecipeDetailActivity.class.getName()));
        intended(hasExtra("extra_recipe_id", 1L));
    }

    public void testOpenRecipeDetail() {

    }
}
