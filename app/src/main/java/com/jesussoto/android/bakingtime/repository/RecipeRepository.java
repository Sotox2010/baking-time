package com.jesussoto.android.bakingtime.repository;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.NonNull;

import com.jesussoto.android.bakingtime.R;
import com.jesussoto.android.bakingtime.api.Resource;
import com.jesussoto.android.bakingtime.api.WebService;
import com.jesussoto.android.bakingtime.appwidget.RecipeIngredientsWidgetProvider;
import com.jesussoto.android.bakingtime.db.dao.BakingStepDao;
import com.jesussoto.android.bakingtime.db.dao.IngredientDao;
import com.jesussoto.android.bakingtime.db.dao.RecipeDao;
import com.jesussoto.android.bakingtime.db.entity.BakingStep;
import com.jesussoto.android.bakingtime.db.entity.Ingredient;
import com.jesussoto.android.bakingtime.db.entity.Recipe;
import com.jesussoto.android.bakingtime.di.qualifier.ApplicationContext;
import com.jesussoto.android.bakingtime.util.AppPreferenceManager;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.schedulers.Schedulers;

@Singleton
public class RecipeRepository {

    private static final long RECIPE_UPDATE_THRESHOLD_MINUTES = 240; // 4 HOURS

    @NonNull
    private WebService mWebService;

    @NonNull
    private RecipeDao mRecipeDao;

    @NonNull
    private IngredientDao mIngredientDao;

    @NonNull
    private BakingStepDao mBakingStepDao;

    @NonNull
    private MutableLiveData<Boolean> mRefreshingState;

    @NonNull
    private Context mAppContext;

    @NonNull
    private AppPreferenceManager mPreferenceManager;

    @Inject
    public RecipeRepository(@NonNull WebService service, @NonNull RecipeDao recipeDao,
                            @NonNull IngredientDao ingredientDao,
                            @NonNull BakingStepDao bakingStepDao,
                            @NonNull @ApplicationContext Context appContext,
                            @NonNull AppPreferenceManager preferenceManager) {
        mWebService = service;
        mRecipeDao = recipeDao;
        mIngredientDao = ingredientDao;
        mBakingStepDao = bakingStepDao;
        mRefreshingState = new MutableLiveData<>();
        mAppContext = appContext;
        mPreferenceManager = preferenceManager;
    }

    @SuppressLint("CheckResult")
    public LiveData<Resource<Recipe>> getRecipeById(long recipeId) {
        return Transformations.map(mRecipeDao.getById(recipeId), Resource::success);
    }

    @SuppressLint("CheckResult")
    public LiveData<Resource<List<Recipe>>> getRecipes() {
        refreshRecipes();
        return Transformations.map(mRecipeDao.getAll(), Resource::success);
    }

    @NonNull
    public LiveData<Resource<List<Ingredient>>> getIngredientsForRecipeId(long recipeId) {
        return Transformations.map(mIngredientDao.getAllByRecipeId(recipeId), Resource::success);
    }

    @NonNull
    public LiveData<Resource<List<BakingStep>>> getBakingStepsForRecipeId(long recipeId) {
        return Transformations.map(mBakingStepDao.getAllByRecipeId(recipeId), Resource::success);
    }

    @NonNull
    public LiveData<Boolean> getRefreshingState() {
        return mRefreshingState;
    }

    @SuppressLint("CheckResult")
    public void refreshRecipes() {
        if (!shouldFetch()) {
            return;
        }

        mRefreshingState.setValue(true);
        mWebService.getRecipes()
                .doFinally(() -> mRefreshingState.postValue(false))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                        // onSuccess
                        this::saveRecipesToDatabase,
                        // onError
                        Throwable::printStackTrace
                );
    }

    private boolean shouldFetch() {
        Date lastUpdate = mPreferenceManager.getRecipesLastUpdatedTime();
        Date now = new Date();

        if (lastUpdate == null) {
            return true;
        }

        long diffInMillis = Math.abs(now.getTime() - lastUpdate.getTime());
        long diffInMinutes = TimeUnit.MINUTES.convert(diffInMillis, TimeUnit.MILLISECONDS);
        return diffInMinutes > RECIPE_UPDATE_THRESHOLD_MINUTES;
    }

    private void saveRecipesToDatabase(List<Recipe> recipes) {
        mRecipeDao.clearTable();
        for (Recipe recipe : recipes) {
            long recipeId = mRecipeDao.insert(recipe);

            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.setRecipeId(recipeId);
            }

            for (BakingStep step : recipe.getSteps()) {
                step.setRecipeId(recipeId);
            }

            mIngredientDao.insert(recipe.getIngredients());
            mBakingStepDao.insert(recipe.getSteps());
        }
        mPreferenceManager.updateRecipesLastUpdatedTime();
        updateWidgets();
    }

    private void updateWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mAppContext);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(
                mAppContext, RecipeIngredientsWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredients_list_view);
    }
}
