package com.arif.cleanarchitecture.network.api;

import android.support.annotation.NonNull;

import com.arif.cleanarchitecture.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * created by arifnadeem
 * <p>
 * Copyright 2017 - Arif Nadeem
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class RetrofitProvider {
    private static RetrofitProvider retrofitProvider;
    private OmdbRestApi mOmdbRestApi;
    private String mBaseUrl = "http://www.omdbapi.com";
    private static final int TIMEOUT = 20;

    private RetrofitProvider() {
        buildRestApi();
    }

    public static RetrofitProvider getInstance() {
        if (retrofitProvider == null) {
            synchronized (RetrofitProvider.class) {
                if (retrofitProvider == null) {
                    retrofitProvider = new RetrofitProvider();
                }
            }
        }
        return retrofitProvider;
    }

    public OmdbRestApi provideApi() {
        return mOmdbRestApi;
    }

    private void buildRestApi() {
        OkHttpClient.Builder okHttpClient = getOkHttpBuilder();
        okHttpClient.readTimeout(TIMEOUT, TimeUnit.SECONDS);
        okHttpClient.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
        if (!BuildConfig.DEBUG) {
            //use some other URL for production
        }
        final Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient.build());
        mOmdbRestApi = builder.build().create(OmdbRestApi.class);
    }

    @NonNull
    private OkHttpClient.Builder getOkHttpBuilder() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient.addInterceptor(logging);
        }
        return okHttpClient;
    }
}

