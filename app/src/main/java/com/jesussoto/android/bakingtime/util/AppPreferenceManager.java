package com.jesussoto.android.bakingtime.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.jesussoto.android.bakingtime.di.qualifier.ApplicationContext;

import javax.inject.Singleton;

@Singleton
public class AppPreferenceManager {

    @NonNull
    private SharedPreferences mSharedPreferences;

    public AppPreferenceManager(@ApplicationContext Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


}
