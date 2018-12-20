package com.jesussoto.android.bakingtime.ui.main;

import android.support.annotation.Nullable;

import com.jesussoto.android.bakingtime.db.entity.Recipe;

import java.util.List;

public class MainUiModel {

    private boolean isLoadingVisible;

    private boolean isRecipeListVisible;

    private boolean isEmptyViewVisible;

    @Nullable
    private List<Recipe> recipes;

    public MainUiModel(boolean isLoadingVisible, boolean isRecipeListVisible, boolean isEmptyViewVisible, @Nullable List<Recipe> recipes) {
        this.isLoadingVisible = isLoadingVisible;
        this.isRecipeListVisible = isRecipeListVisible;
        this.isEmptyViewVisible = isEmptyViewVisible;
        this.recipes = recipes;
    }


    @Nullable
    public List<Recipe> getRecipes() {
        return recipes;
    }

    public boolean isLoadingVisible() {
        return isLoadingVisible;
    }

    public boolean isRecipeListVisible() {
        return isRecipeListVisible;
    }

    public boolean isEmptyViewVisible() {
        return isEmptyViewVisible;
    }
}
