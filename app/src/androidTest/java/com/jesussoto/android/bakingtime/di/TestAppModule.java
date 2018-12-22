package com.jesussoto.android.bakingtime.di;

import android.support.annotation.NonNull;

import com.jesussoto.android.bakingtime.api.WebService;
import com.jesussoto.android.bakingtime.di.module.AppModule;

import org.mockito.Mockito;

import okhttp3.OkHttpClient;

public class TestAppModule extends AppModule {

    private WebService mMockWebService;

    public TestAppModule(WebService mockWebService) {
        mMockWebService = mockWebService;
    }

    @NonNull
    @Override
    public WebService provideWebService(OkHttpClient.Builder clientBuilder) {
        return mMockWebService;
    }
}
