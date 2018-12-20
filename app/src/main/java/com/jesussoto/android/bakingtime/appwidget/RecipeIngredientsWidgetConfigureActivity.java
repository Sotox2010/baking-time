package com.jesussoto.android.bakingtime.appwidget;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.jesussoto.android.bakingtime.R;
import com.jesussoto.android.bakingtime.api.Resource;
import com.jesussoto.android.bakingtime.db.AppDatabase;
import com.jesussoto.android.bakingtime.db.entity.Recipe;
import com.jesussoto.android.bakingtime.ui.main.OnRecipeTappedListener;
import com.jesussoto.android.bakingtime.util.IngredientsWidgetInfo;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

/**
 * The configuration screen for the {@link RecipeIngredientsWidgetProvider} AppWidget.
 */
public class RecipeIngredientsWidgetConfigureActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "com.jesussoto.android.bakingtime.appwidget.RecipeIngredientsWidgetProvider";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Inject
    AppDatabase mDatabase;

    OnRecipeTappedListener mOnRecipeTappedListener = recipe -> {
        final Context context = RecipeIngredientsWidgetConfigureActivity.this;

        // When the button is clicked, store the string locally
        // String widgetText = mAppWidgetText.getText().toString();
        saveWidgetInfo(context, mAppWidgetId, recipe);

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RecipeIngredientsWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    };

    @Inject
    ViewModelProvider.Factory mFactory;

    @BindView(R.id.recipes_recycler_view)
    RecyclerView mRecipesRecyclerView;

    RecipeIngredientWidgetAdapter mAdapter;

    public RecipeIngredientsWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveWidgetInfo(Context context, int appWidgetId, Recipe recipe) {
        IngredientsWidgetInfo info = new IngredientsWidgetInfo(recipe.getId(), recipe.getName());
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, new Gson().toJson(info));
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static IngredientsWidgetInfo loadWidgetInfo(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String json = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        return new Gson().fromJson(json, IngredientsWidgetInfo.class);
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        AndroidInjection.inject(this);
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.recipe_ingredients_widget_provider_configure);
        ButterKnife.bind(this);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        setupRecyclerView();
        bindViewModel();
    }

    private void setupRecyclerView() {
        mAdapter = new RecipeIngredientWidgetAdapter(null, "Select a recipe");
        mAdapter.setOnRecipeTappedListener(mOnRecipeTappedListener);

        mRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecipesRecyclerView.setAdapter(mAdapter);

    }

    private void bindViewModel() {
        RecipeIngredientsWidgetConfigureViewModel viewModel = ViewModelProviders.of(this, mFactory)
                .get(RecipeIngredientsWidgetConfigureViewModel.class);

        viewModel.getRecipes().observe(this, this::updateRecipes);
    }

    private void updateRecipes(Resource<List<Recipe>> recipesResource) {
        List<Recipe> recipes = recipesResource.getData();
        mAdapter.replaceData(recipes);

        /*if (recipes.isEmpty()) {
            Toast.makeText(this, "There are no available recipes, open the main app first", Toast.LENGTH_LONG).show();
            finish();
        }*/
    }

}
