package com.jesussoto.android.bakingtime.appwidget;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.jesussoto.android.bakingtime.api.Resource;
import com.jesussoto.android.bakingtime.db.entity.Recipe;
import com.jesussoto.android.bakingtime.repository.RecipeRepository;

import java.util.List;

import javax.inject.Inject;

public class RecipeIngredientsWidgetConfigureViewModel extends ViewModel {

    private RecipeRepository mRepository;

    @Inject
    RecipeIngredientsWidgetConfigureViewModel(RecipeRepository repository) {
        mRepository = repository;
    }

    LiveData<Resource<List<Recipe>>> getRecipes() {
        return mRepository.getRecipes();
    }
}
