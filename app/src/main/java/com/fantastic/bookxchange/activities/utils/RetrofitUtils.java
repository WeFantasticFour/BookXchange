package com.fantastic.bookxchange.activities.utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gretel on 10/12/17.
 */

public class RetrofitUtils {

    public static Retrofit get() {
        return new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(client())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static OkHttpClient client() {
        return new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                // .addInterceptor(loggingInterceptor())
                .build();
    }

    private static HttpLoggingInterceptor loggingInterceptor() {
        // TODO: Insert your code here
        return null;
    }

}
