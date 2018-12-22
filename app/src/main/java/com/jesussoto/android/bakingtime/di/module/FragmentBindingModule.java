package com.jesussoto.android.bakingtime.di.module;

import com.jesussoto.android.bakingtime.di.scope.FragmentScope;
import com.jesussoto.android.bakingtime.ui.main.MainFragment;
import com.jesussoto.android.bakingtime.ui.recipedetail.RecipeDetailFragment;
import com.jesussoto.android.bakingtime.ui.recipedetail.RecipeStepFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBindingModule {

    @FragmentScope
    @ContributesAndroidInjector()
    abstract MainFragment contributeMainFragment();

    @FragmentScope
    @ContributesAndroidInjector()
    abstract RecipeDetailFragment contributeRecipeDetailFragment();

    @FragmentScope
    @ContributesAndroidInjector()
    abstract RecipeStepFragment contributeRecipeStepFragment();
}
