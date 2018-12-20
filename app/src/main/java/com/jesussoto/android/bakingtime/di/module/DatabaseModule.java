package com.jesussoto.android.bakingtime.di.module;

import android.content.Context;
import android.support.annotation.NonNull;

import android.arch.persistence.room.Room;

import com.jesussoto.android.bakingtime.db.AppDatabase;
import com.jesussoto.android.bakingtime.db.dao.BakingStepDao;
import com.jesussoto.android.bakingtime.db.dao.IngredientDao;
import com.jesussoto.android.bakingtime.db.dao.RecipeDao;
import com.jesussoto.android.bakingtime.di.qualifier.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @Singleton
    @Provides
    @NonNull
    public AppDatabase provideDatabase(@ApplicationContext Context appContext) {
        return Room.databaseBuilder(appContext, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    @NonNull
    public RecipeDao provideRecipeDao(AppDatabase database) {
        return database.recipeDao();
    }

    @Singleton
    @Provides
    @NonNull
    public IngredientDao provideIngredientDao(AppDatabase database) {
        return database.ingredientDao();
    }

    @Singleton
    @Provides
    @NonNull
    public BakingStepDao provideBakingStepDao(AppDatabase database) {
        return database.bakingStepDao();
    }
}
