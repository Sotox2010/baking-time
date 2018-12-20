package com.jesussoto.android.bakingtime.di.module;

import com.jesussoto.android.bakingtime.appwidget.IngredientsWidgetService;
import com.jesussoto.android.bakingtime.appwidget.RecipeIngredientsWidgetConfigureActivity;
import com.jesussoto.android.bakingtime.appwidget.RecipeIngredientsWidgetProvider;
import com.jesussoto.android.bakingtime.di.scope.ActivityScope;
import com.jesussoto.android.bakingtime.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServiceBindingModule {

    @ContributesAndroidInjector()
    abstract IngredientsWidgetService contributeIngredientsWidgetService();
}
