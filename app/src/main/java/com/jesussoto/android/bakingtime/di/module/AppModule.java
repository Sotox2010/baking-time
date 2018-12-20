package com.jesussoto.android.bakingtime.di.module;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.jesussoto.android.bakingtime.BuildConfig;
import com.jesussoto.android.bakingtime.api.WebService;
import com.jesussoto.android.bakingtime.di.qualifier.ApplicationContext;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {

    @Singleton
    @Provides
    @ApplicationContext
    @NonNull
    public Context provideApplicationContext(Application app) {
        return app.getApplicationContext();
    }

    @Provides
    @NonNull
    public OkHttpClient.Builder provideOkHttpClientBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        return builder;
    }

    @Singleton
    @Provides
    @NonNull
    public WebService provideWebService(OkHttpClient.Builder clientBuilder) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(clientBuilder.build())
                .build()
                .create(WebService.class);
    }

    @Singleton
    @Provides
    @NonNull
    public Picasso providePicasso(@ApplicationContext Context appContext,
                                  OkHttpClient.Builder clientBuilder) {

        return new Picasso.Builder(appContext)
                .downloader(new OkHttp3Downloader(clientBuilder.build()))
                .build();
    }

}
