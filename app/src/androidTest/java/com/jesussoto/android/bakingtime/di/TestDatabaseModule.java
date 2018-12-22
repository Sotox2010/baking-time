package com.jesussoto.android.bakingtime.di;

import android.content.Context;
import android.support.annotation.NonNull;

import com.jesussoto.android.bakingtime.db.AppDatabase;
import com.jesussoto.android.bakingtime.di.module.DatabaseModule;
import com.jesussoto.android.bakingtime.di.qualifier.ApplicationContext;

import dagger.Module;

@Module
public class TestDatabaseModule extends DatabaseModule {

    @ApplicationContext
    private Context mAppContext;

    public TestDatabaseModule(@ApplicationContext Context context) {
        mAppContext = context;
    }
}
