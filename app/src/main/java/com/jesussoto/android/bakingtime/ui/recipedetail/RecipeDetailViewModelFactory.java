package com.jesussoto.android.bakingtime.ui.recipedetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.jesussoto.android.bakingtime.db.entity.BakingStep;
import com.jesussoto.android.bakingtime.db.entity.Recipe;
import com.jesussoto.android.bakingtime.repository.RecipeRepository;

public class RecipeDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private long mRecipeId;
    private RecipeRepository mRepository;

    RecipeDetailViewModelFactory(long recipeId, RecipeRepository repository) {
        mRecipeId = recipeId;
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RecipeDetailViewModel(mRecipeId, mRepository);
    }
}
