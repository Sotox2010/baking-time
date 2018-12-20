package com.jesussoto.android.bakingtime.di.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.jesussoto.android.bakingtime.appwidget.RecipeIngredientsWidgetConfigureViewModel;
import com.jesussoto.android.bakingtime.di.ViewModelFactory;
import com.jesussoto.android.bakingtime.di.ViewModelKey;
import com.jesussoto.android.bakingtime.ui.main.MainViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindMainViewModel(MainViewModel moviesViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RecipeIngredientsWidgetConfigureViewModel.class)
    abstract ViewModel bindMovieDetailViewModel(
            RecipeIngredientsWidgetConfigureViewModel recipeIngredientsWidgetConfigureViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
