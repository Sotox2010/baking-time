package com.jesussoto.android.bakingtime.di.module;

import com.jesussoto.android.bakingtime.appwidget.RecipeIngredientsWidgetConfigureActivity;
import com.jesussoto.android.bakingtime.di.scope.ActivityScope;
import com.jesussoto.android.bakingtime.ui.main.MainActivity;
import com.jesussoto.android.bakingtime.ui.recipedetail.RecipeDetailActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector()
    abstract MainActivity contributeMainActivity();

    @ActivityScope
    @ContributesAndroidInjector()
    abstract RecipeDetailActivity contributeRecipeDetailActivity();

    @ActivityScope
    @ContributesAndroidInjector()
    abstract RecipeIngredientsWidgetConfigureActivity contributeWidgetConfigureActivity();
}
