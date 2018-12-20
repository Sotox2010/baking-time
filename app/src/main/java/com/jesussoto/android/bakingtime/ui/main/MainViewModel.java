package com.jesussoto.android.bakingtime.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.jesussoto.android.bakingtime.api.Resource;
import com.jesussoto.android.bakingtime.db.entity.Recipe;
import com.jesussoto.android.bakingtime.repository.RecipeRepository;

import java.util.List;

import javax.inject.Inject;

public class MainViewModel extends ViewModel {

    @NonNull
    private RecipeRepository mRepository;

    @NonNull
    private LiveData<Resource<List<Recipe>>> mRecipesLiveData;

    @Inject
    public MainViewModel(@NonNull RecipeRepository repository) {
        mRepository = repository;
        mRecipesLiveData = mRepository.getRecipes();
    }

    @NonNull
    LiveData<MainUiModel> getUiModel() {
        return Transformations.map(mRecipesLiveData, this::constructUiModel);
    }

    void retry() {
        mRepository.refreshRecipes();
    }

    LiveData<Boolean> getRefreshingState() {
        return mRepository.getRefreshingState();
    }

    private MainUiModel constructUiModel(Resource<List<Recipe>> resource) {
        boolean isLoadingVisible = resource.getStatus() == Resource.Status.LOADING;
        boolean isRecipeListVisible = resource.getStatus() == Resource.Status.SUCCESS;
        boolean isErrorVisible = resource.getStatus() == Resource.Status.ERROR;

        return new MainUiModel(isLoadingVisible, isRecipeListVisible,
                isErrorVisible, resource.getData());
    }
}
