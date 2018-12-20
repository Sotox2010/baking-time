package com.jesussoto.android.bakingtime.di;

import android.arch.lifecycle.ViewModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import dagger.MapKey;

@MapKey
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewModelKey {
    Class<? extends ViewModel> value();
}
