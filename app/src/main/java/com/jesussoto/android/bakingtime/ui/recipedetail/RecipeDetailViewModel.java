package com.jesussoto.android.bakingtime.ui.recipedetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.jesussoto.android.bakingtime.api.Resource;
import com.jesussoto.android.bakingtime.db.entity.BakingStep;
import com.jesussoto.android.bakingtime.db.entity.Ingredient;
import com.jesussoto.android.bakingtime.db.entity.Recipe;
import com.jesussoto.android.bakingtime.repository.RecipeRepository;
import com.jesussoto.android.bakingtime.util.SingleLiveEvent;

import java.util.List;

class RecipeDetailViewModel extends ViewModel {

    private long mRecipeId;

    @NonNull
    private RecipeRepository mRepository;

    @NonNull
    private LiveData<Resource<Recipe>> mRecipeData;

    @NonNull
    private LiveData<Resource<List<Ingredient>>> mIngredientsData;

    @NonNull
    private LiveData<Resource<List<BakingStep>>> mStepsData;

    @NonNull
    private SingleLiveEvent<BakingStep> mSelectedStepData;

    RecipeDetailViewModel(long recipeId, @NonNull RecipeRepository repository) {
        mRecipeId = recipeId;
        mRepository = repository;

        mRecipeData = repository.getRecipeById(recipeId);
        mIngredientsData = repository.getIngredientsForRecipeId(recipeId);
        mStepsData = repository.getBakingStepsForRecipeId(recipeId);
        mSelectedStepData = new SingleLiveEvent<>();
    }

    @NonNull
    LiveData<Resource<Recipe>> getRecipe() {
        return mRecipeData;
    }

    @NonNull
    LiveData<Resource<List<Ingredient>>> getIngredients() {
        return mIngredientsData;
    }

    @NonNull
    LiveData<Resource<List<BakingStep>>> getBakingSteps() {
        return mStepsData;
    }

    @NonNull
    LiveData<BakingStep> getSelectedStep() {
        return mSelectedStepData;
    }

    void selectStep(BakingStep step) {
        if (step != mSelectedStepData.getValue()) {
            mSelectedStepData.setValue(step);
        }
    }
}
