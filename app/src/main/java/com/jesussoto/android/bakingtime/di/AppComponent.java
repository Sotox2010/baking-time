package com.jesussoto.android.bakingtime.di;

import android.app.Application;

import com.jesussoto.android.bakingtime.BakingTimeApp;
import com.jesussoto.android.bakingtime.di.module.ActivityBindingModule;
import com.jesussoto.android.bakingtime.di.module.AppModule;
import com.jesussoto.android.bakingtime.di.module.ServiceBindingModule;
import com.jesussoto.android.bakingtime.di.module.DatabaseModule;
import com.jesussoto.android.bakingtime.di.module.FragmentBindingModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(
    modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        DatabaseModule.class,
        ActivityBindingModule.class,
        FragmentBindingModule.class,
        ServiceBindingModule.class
    }
)
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(BakingTimeApp app);
}
