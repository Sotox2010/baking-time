package com.jesussoto.android.bakingtime.di;

import com.jesussoto.android.bakingtime.di.module.ActivityBindingModule;
import com.jesussoto.android.bakingtime.di.module.AppModule;
import com.jesussoto.android.bakingtime.di.module.DatabaseModule;
import com.jesussoto.android.bakingtime.di.module.FragmentBindingModule;
import com.jesussoto.android.bakingtime.di.module.ServiceBindingModule;
import com.jesussoto.android.bakingtime.ui.main.MainActivity;

import javax.inject.Singleton;

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
public interface TestAppComponent extends AppComponent {

    @Component.Builder
    interface Builder {
        AppComponent build();
//        Builder androidInjectionModule(AndroidInjectionModule module);
        Builder appModule(AppModule appModule);
//        Builder databaseModule(DatabaseModule dbModule);
//        Builder activityBindingModule(ActivityBindingModule module);
//        Builder fragmentBindingModule(FragmentBindingModule module);
//        Builder serviceBindingModule(ServiceBindingModule module);
    }

//    void inject(MainActivity activity);
}
