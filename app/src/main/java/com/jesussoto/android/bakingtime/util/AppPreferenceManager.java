package com.jesussoto.android.bakingtime.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jesussoto.android.bakingtime.di.qualifier.ApplicationContext;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppPreferenceManager {

    private static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    private static final String KEY_LAST_RECIPE_UPDATE_TIME = "key_last_recipe_update_time";

    @NonNull
    private SharedPreferences mSharedPreferences;

    @Inject
    public AppPreferenceManager(@ApplicationContext Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Nullable
    public Date getRecipesLastUpdatedTime() {
        String dateString = mSharedPreferences.getString(KEY_LAST_RECIPE_UPDATE_TIME, null);
        if (dateString == null) {
            return null;
        }

        SimpleDateFormat format = new SimpleDateFormat(DATETIME_FORMAT, Locale.US);

        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    public void updateRecipesLastUpdatedTime() {
        SimpleDateFormat format = new SimpleDateFormat(DATETIME_FORMAT, Locale.US);
        mSharedPreferences.edit().putString(KEY_LAST_RECIPE_UPDATE_TIME,
                format.format(new Date())).apply();
    }
}
